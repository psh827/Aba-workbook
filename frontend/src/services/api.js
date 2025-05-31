// src/services/api.js
import axios from 'axios';
import { AuthContext } from '../context/AuthContext';
import React, { useContext } from 'react';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
});

// accessToken 자동 포함
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

// 자동 갱신 인터셉터
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        const loginId = localStorage.getItem("loginId");
        const loginType = localStorage.getItem("loginType");
        const constraintKey = localStorage.getItem("constraintKey");

        const res = await axios.post("http://localhost:8080/token/refresh", {
          loginId,
          refreshToken,
          loginType,
          constraintKey
        });

        const newAccessToken = res.data.accessToken;
        const newRefreshToken = res.data.refreshToken;

        localStorage.setItem("accessToken", newAccessToken);
        localStorage.setItem("refreshToken", newRefreshToken);

        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (err) {
        window.location.href = '/'; // refresh 실패 → 로그인 페이지 이동
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
