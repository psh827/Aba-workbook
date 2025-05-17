import React from "react";
import protestKeyMap from "../protestKeyMap";

const BXFunctionSummary = ({ functionCounts }) => {
  const functionSummary = Object.entries(functionCounts).map(([label, count]) => ({
    label,
    key: Object.keys(protestKeyMap).find(k => protestKeyMap[k][0] === label) || "",
    count
  }));

  return functionSummary.length > 0 ? (
    <div style={{ marginTop: "10px", maxWidth: "600px" }}>
      <h3>🧠 기능 요약</h3>
      <table className="aba-table">
        <thead>
          <tr>
            <th>기능</th>
            <th>코드</th>
            <th>Count</th>
          </tr>
        </thead>
        <tbody>
          {functionSummary.map((item, idx) => (
            <tr key={idx}>
              <td>{item.label}</td>
              <td>{item.key}</td>
              <td>{item.count}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  ) : null;
};

export default BXFunctionSummary;
