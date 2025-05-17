import React from "react";
import "../../css/programs_result/ProgramsGrid.css"; // 스타일 분리 (선택사항)

const chunkArray = (arr, size) => {
  return arr.reduce((acc, _, i) => {
    if (i % size === 0) acc.push(arr.slice(i, i + size));
    return acc;
  }, []);
};

const ProgramsSection = ({ data }) => {
  const validPrograms = Object.values(data).filter(
    (program) => program?.items && program.items.length > 0
  );

  return (
    <div className="programs-container">
      {validPrograms.map((program, idx) => (
        <div key={idx} className="program-card">
          <h3 className="program-title">📘 {program.program_name}</h3>
          <p>시팅 수: {program.session_cnt}</p>

          <div className="item-grid-container">
            <div className="item-grid-header">
              <span className="item-col">아이템</span>
              <span className="item-col">강도</span>
              <span className="item-col">TSM</span>
            </div>
            <div className="program-items-grouped">
              <div className="item-list-grid">
                {program.items.map((item, idx) => (
                  <div key={idx} className="item-card">
                    <div className="item-name">{item.item}</div>
                    <div className="item-info">ST: {item.strength}</div>
                    <div className="item-info">TSM: {item.tsm}</div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {program.notes?.length > 0 && (
            <div className="program-notes">
              <strong>📌 메모:</strong>
              <ul>
                {program.notes.map((note, i) => (
                  <li key={i}>{note}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};
export default ProgramsSection;
