// BXProtestSection.jsx
import React from "react";
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";
import { formatToHHMMSS } from "../../utils/common";
import { generateLongProtestPieData, generateColorMapFromData } from "../..//utils/fileUploadChart";
import { CustomTooltip, renderCustomLegend } from "../../utils/CustomLegend";
import protestKeyMap from "../protestKeyMap";

const 기능키 = ["escape", "access", "attention", "alone/play"];

const BXProtestSection = ({ data, type, functionCounts }) => {
  const value = data[type];
  if (!value) return null;
  const 기능키 = ["escape", "access", "attention", "alone/play"];
  const roundChartData = generateLongProtestPieData(value);
  const colorMap = generateColorMapFromData(data);

  const this_week_avg_time = formatToHHMMSS(value?.this_week_avg_time?.time);
  const past_week_avg_time = formatToHHMMSS(value?.past_week_avg_time?.time);
  const this_week_cnt = value?.this_week_cnt?.count ?? "";
  const past_week_cnt = value?.past_week_cnt?.count ?? "";

  const up_down_val =
    this_week_cnt - past_week_cnt > 0 ? "증가" :
    this_week_cnt - past_week_cnt < 0 ? "감소" :
    "변화 없음";

  return (
    <div key={type} style={{ display: "flex", alignItems: "center" }}>
      {/* ✅ 왼쪽: 표 */}
      <div className="table-section" style={{ flex: 1 }}>
        <h2>{type}</h2>

        {/* ✅ 요약 테이블 */}
        <table className="aba-table">
          <thead>
            <tr>
              <th>이번주 수</th>
              <th>이번 평균 시간</th>
              <th>지난 주 수</th>
              <th>지난 주 평균 시간</th>
              <th>증감여부</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{this_week_cnt}</td>
              <td>{this_week_avg_time}</td>
              <td>{past_week_cnt}</td>
              <td>{past_week_avg_time}</td>
              <td>{up_down_val}</td>
            </tr>
          </tbody>
        </table>

        {/* ✅ 상세 행동/기능 테이블 */}
        <table className="aba-table" style={{ marginTop: "20px" }}>
          <thead>
            <tr>
              <th>구분</th>
              <th>행동</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(value).map(([behavior, raw]) => {
              if (raw.action && (!raw.count || raw.count.trim() === "")) return null;
              
              if (raw.function_item && 기능키.includes(raw.function_item)) {
                const label = protestKeyMap[raw.function_item]?.[0] || raw.function_item;
                const count = parseInt(raw.function_item_cnt || 0, 10);
                functionCounts[label] = (functionCounts[label] || 0) + count;
              }

              if (raw.action && raw.count?.trim() !== "") {
                return (
                  <tr key={behavior}>
                    <td>동작</td>
                    <td>{protestKeyMap[behavior]?.[0] || behavior} ({behavior})</td>
                    <td>{raw.count}</td>
                  </tr>
                );
              }

              return null;
            })}
          </tbody>
        </table>
      </div>

      {/* ✅ 오른쪽: 원형 차트 */}
      <div style={{ flex: 1, display: "flex", justifyContent: "center" }}>
        <div style={{ width: "400px", height: "400px" }}>
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={roundChartData}
                cx="50%"
                cy="50%"
                outerRadius={120}
                label
                dataKey="value"
              >
                {roundChartData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={colorMap[entry.name]} />
                ))}
              </Pie>
              <Tooltip content={CustomTooltip} />
              <Legend verticalAlign="bottom" align="center" content={renderCustomLegend} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default BXProtestSection;
