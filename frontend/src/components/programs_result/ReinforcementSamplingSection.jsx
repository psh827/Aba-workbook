import React from "react";
import "../../css/programs_result/ReinforcementSamplingSection.css";

const ReinforcementSamplingSection = ({ data }) => {
  const renderRatioRow = (row) => {
    if (!row || (!row.ratios_text && !row.ratios_suc && !row.ratios_attemp)) return null;
    return (
      <div className="ratio-row">
        {row.ratios_text && (
          <span className="ratio-type">{row.ratios_text}</span>
        )}
        {row.ratios_suc && row.ratios_attemp && (
          <span className="ratio-values">
            {row.ratios_suc} / {row.ratios_attemp}
          </span>
        )}
      </div>
    );
  };

  return (
    <div className="sampling-container">
      {data
        .filter((item) => item.title)
        .map((item, idx) => {
          const { title, detail_sampling, sampling_info, first_row, second_row } = item;
          return (
            <div className="sampling-card" key={idx}>
              <div className="sampling-header">
                <h3>📘 {title}</h3>
                {detail_sampling && <div className="sub-label">💬 {detail_sampling}</div>}
              </div>

              <div className="sampling-body">
                {renderRatioRow(first_row)}
                {renderRatioRow(second_row)}

                <div className="sampling-info">
                  {sampling_info?.session_cnt && (
                    <span className="info-badge">시팅 수: {sampling_info.session_cnt}</span>
                  )}
                  {sampling_info?.MI && (
                    <span className="info-badge">MI: {sampling_info.MI}</span>
                  )}
                  {sampling_info?.tsm && (
                    <span className="info-badge">TSM: {sampling_info.tsm}</span>
                  )}
                </div>
              </div>

              <div className="sampling-notes">
                {first_row?.note1 && <p>📝 {first_row.note1}</p>}
                {first_row?.note2 && <p>📝 {first_row.note2}</p>}
                {second_row?.note1 && <p>📝 {second_row.note1}</p>}
                {second_row?.note2 && <p>📝 {second_row.note2}</p>}
              </div>
            </div>
          );
        })}
    </div>
  );
};

export default ReinforcementSamplingSection;
