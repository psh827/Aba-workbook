import React from "react";
import "../../css/programs_result/PromptingHierarchy.css";

const PromptingHierarchy = ({ data }) => {
  const filteredData = data.filter(
    (item) => item?.category && item.activities.some((act) => act.step)
  );

  return (
    <div className="prompting-container">
      {filteredData.map((entry, idx) => (
        <div key={idx} className="prompting-card">
          <div className="prompting-header">
            <span role="img" aria-label="book">📘</span>{" "}
            <strong>{entry.category}{entry.sub_category ? ` - ${entry.sub_category}` : ""}</strong>
          </div>
          <div className="prompting-meta">
            시팅 수: {entry.ratios?.second_ratio?.session_cnt ?? 0} &nbsp;
            TSM: {entry.ratios?.second_ratio?.tsm ?? 0}
          </div>
          {entry.activities[0]?.note && (
            <div className="prompting-note">
              <span role="img" aria-label="memo">📝</span>{" "}
              {entry.activities[0].note}
            </div>
          )}
          <table className="prompting-table">
            <thead>
              <tr>
                <th>STEP</th>
                <th>IND</th>
                <th>HIP</th>
                <th>NOTE</th>
              </tr>
            </thead>
            <tbody>
              {entry.activities
                .filter((a) => a.step)
                .map((step, i) => (
                  <tr key={i}>
                    <td>{step.step}</td>
                    <td>{step.ind ?? "-"}</td>
                    <td>{step.hip ?? "-"}</td>
                    <td>{step.note ?? "-"}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      ))}
    </div>
  );
};

export default PromptingHierarchy;
