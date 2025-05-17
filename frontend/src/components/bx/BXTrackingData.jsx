import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";
import { generateBarChartData } from "../../utils/fileUploadChart";

const BXTrackingData = ({ data }) => {
  return (
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
      <div className="table-section">
        <table className="aba-table">
          <thead>
            <tr>
              <th>행동</th>
              <th>시도횟수</th>
              <th>세션수</th>
              <th>세션당 횟수</th>
              <th>지난 주 세션당 횟수</th>
              <th>증감여부</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(data).map(([key, value]) => {
              if (
                key !== 'non-responses' &&
                (value.behavior?.trim() === "" || value.behavior === null)
              ) {
                return null;
              }

              const result_past_week_num = typeof value.result_past_week === "string"
                ? value.result_past_week.replace("/ssn", "").replace("%", "")
                : "";

              const up_and_down_val = Number(value.calculate) - Number(result_past_week_num);
              const up_down = up_and_down_val > 0 ? "증가" : up_and_down_val < 0 ? "감소" : "변화 없음";
              const trials_in_session = Math.floor(value.trials_in_session);
              const calculate = Number(value.calculate).toFixed(2);

              return key === "non-responses" ? (
                <tr key={key}>
                  <td>non-responses</td>
                  <td>{value.behavior_trials}</td>
                  <td>{trials_in_session}</td>
                  <td>{calculate}</td>
                  <td>{value.result_past_week}</td>
                  <td>{up_down}</td>
                </tr>
              ) : (
                <tr key={key}>
                  <td>{value.behavior}</td>
                  <td>{value.behavior_trials}</td>
                  <td>{trials_in_session}</td>
                  <td>{calculate}</td>
                  <td>{result_past_week_num}</td>
                  <td>{up_down}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* ✅ 시각화 영역 (오른쪽) */}
      <div style={{ flex: 1 }}>
        {generateBarChartData(data).length > 0 ? (
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={generateBarChartData(data)} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="lastWeek" fill="#8884d8" name="지난주" />
              <Bar dataKey="thisWeek" fill="#82ca9d" name="이번주" />
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <p>📭 차트에 표시할 데이터가 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default BXTrackingData;
