import React, { useEffect, useState } from "react";
import { getTrackingBehavior } from "../services/view/api"; // API 호출 함수
import "../css/view/ViewPage.css"; // 선택 사항: 스타일 분리

const ViewPage = () => {
  const [trackingData, setTrackingData] = useState([]);

  useEffect(() => {
    // 진입 시 자동 실행
    fetchTrackingData();
  }, []);

  const fetchTrackingData = async () => {
    try {
      const response = await getTrackingBehavior();
      setTrackingData(response.data); // 예: [{behavior: "트름", success: 3, attempt: 5}, ...]
    } catch (error) {
      console.error("📛 Tracking 데이터 조회 실패:", error);
    }
  };

  return (
    <div className="view-container">
      <h2 className="child-name">아동이름 : 김단우</h2>

      <div className="tracking-section">
        <h3>📊 행동 추적 비교</h3>
        {trackingData.length > 0 ? (
          <table className="tracking-table">
            <thead>
              <tr>
                <th>행동명</th>
                <th>성공 횟수</th>
                <th>시도 횟수</th>
                <th>성공률</th>
              </tr>
            </thead>
            <tbody>
              {trackingData.map((item, index) => (
                <tr key={index}>
                  <td>{item.behavior}</td>
                  <td>{item.success}</td>
                  <td>{item.attempt}</td>
                  <td>
                    {item.attempt > 0
                      ? ((item.success / item.attempt) * 100).toFixed(1) + "%"
                      : "-"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>데이터가 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default ViewPage;
