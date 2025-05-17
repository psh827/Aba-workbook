// src/components/bx/BXTreatmentInfoSection.jsx

import React from "react";
import "../TableStyles.css"; // 필요한 경우 스타일 경로 맞춰 조정하세요

const BXTreatmentInfoSection = ({ data }) => {
  return (
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
      <div className="table-section">
        {/* 세션 위치 테이블 */}
        <table className="aba-table">
          <thead>
            <tr>
              <th>세션 위치</th>
              <th>세션 수</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(data).map(([key, value]) => {
              if (!value?.count?.trim()) return null;
              return (
                <tr key={key}>
                  <td>{value.action}</td>
                  <td>{value.count}</td>
                </tr>
              );
            })}
          </tbody>
        </table>

        {/* 선생님 테이블 */}
        <table className="aba-table" style={{ marginTop: "20px" }}>
          <thead>
            <tr>
              <th>선생님</th>
              <th>세션 수</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(data).flatMap(([key, value]) => {
              if (!value?.th1_cnt?.trim()) return [];
              const rows = [];
              Object.keys(value).forEach((k) => {
                if (k.startsWith("th") && !k.includes("cnt")) {
                  const teacherName = value[k];
                  const countKey = `${k}_cnt`;
                  const teacherCount = value[countKey];

                  if (teacherName && teacherCount) {
                    rows.push(
                      <tr key={`${key}-${k}`}>
                        <td>{teacherName}</td>
                        <td>{teacherCount}</td>
                      </tr>
                    );
                  }
                }
              });
              return rows;
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default BXTreatmentInfoSection;
