import React from "react";
import "../../css/programs_result/PromptingHierarchy2.css";

const PromptingHierarchy2 = ({ data }) => {
  const filteredData = data.filter((entry) => entry?.category);

  return (
    <div className="prompting2-container">
      {filteredData.map((entry, idx) => {
        const hasNotes = entry.activities.some((a) => a.note);

        // HIP 값이 실제 숫자일 경우만 추출
        const hipItems = entry.activities
          .filter((a) => {
            const value = a.hip;
            return value && !isNaN(parseFloat(value));
          })
          .map((a) =>
            a.step ? `${a.step}: HIP ${a.hip}` : `HIP ${a.hip}`
          );

        const firstRatioText = entry.ratios?.first_ratio?.text;
        const secondRatioText = entry.ratios?.second_ratio?.text;
        const firstRatioVal = entry.ratios?.first_ratio;
        const secondRatioVal = entry.ratios?.second_ratio;

        return (
          <div className="prompting2-card" key={idx}>
            <div className="prompting2-header">
              📘 <strong>{entry.category}{entry.sub_category ? ` - ${entry.sub_category}` : ""}</strong>
            </div>

            <div className="prompting2-meta">
              {firstRatioText && firstRatioVal ? (
                <div>
                  {firstRatioText} {firstRatioVal.success_cnt ?? 0}/{firstRatioVal.attemp_cnt ?? 0}
                </div>
              ) : null}

              {secondRatioText && secondRatioVal ? (
                <div>
                  {secondRatioText} {secondRatioVal.success_cnt ?? 0}/{secondRatioVal.attemp_cnt ?? 0}
                </div>
              ) : null}

              {hipItems.length > 0 && (
                <div>{hipItems.join(", ")}</div>
              )}

              <div>
                시팅 수: {secondRatioVal?.session_cnt ?? 0} &nbsp; TSM: {secondRatioVal?.tsm ?? 0}
              </div>
            </div>

            <ul className="prompting2-note-list">
              {hasNotes &&
                entry.activities
                  .filter((a) => a.note)
                  .map((a, i) => (
                    <li key={i}>📜 {a.note}</li>
                  ))}
            </ul>
          </div>
        );
      })}
    </div>
  );
};

export default PromptingHierarchy2;
