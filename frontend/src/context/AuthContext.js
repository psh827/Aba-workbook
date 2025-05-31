import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';
import api from '../services/api'; // axios instance with interceptors

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    loginId: localStorage.getItem("loginId"),
    loginType: localStorage.getItem("loginType"),
    constraintKey: localStorage.getItem("constraintKey"),
    accessToken: localStorage.getItem("accessToken"),
    refreshToken: localStorage.getItem("refreshToken"),
    isAuthenticated: !!localStorage.getItem("accessToken")
  });

  const login = (tokens, userInfo) => {
    const { accessToken, refreshToken } = tokens;
    const { loginId, loginType, constraintKey } = userInfo;

    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    localStorage.setItem("loginId", loginId);
    localStorage.setItem("loginType", loginType);
    localStorage.setItem("constraintKey", constraintKey);

    setAuth({
      accessToken,
      refreshToken,
      loginId,
      loginType,
      constraintKey,
      isAuthenticated: true
    });
  };

  const logout = () => {
    localStorage.clear();
    setAuth({
      accessToken: null,
      refreshToken: null,
      loginId: null,
      loginType: null,
      constraintKey: null,
      isAuthenticated: false
    });
    window.location.href = "/";
  };

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
