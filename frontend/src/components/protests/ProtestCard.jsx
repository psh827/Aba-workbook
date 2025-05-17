import React from "react";
import "../../css/protests/ProtestCard.css"; // 스타일 분리

const ProtestCard = ({ data }) => {
  if (!data || data.length === 0) return <p>데이터가 없습니다.</p>;
  // 💡 유효한 데이터만 필터링: behaviors, date, staff, location, time, function, duration, happened_before 중 하나라도 있으면 표시
  const filtered = data.filter(item => {
    const hasBehavior = item.behaviors && Object.values(item.behaviors).some(v => v !== null);
    const hasMeta =
      item.date || item.staff || item.location || item.time || item.function || item.duration?.min || item.happened_before;
    return hasBehavior || hasMeta;
  });

  if (filtered.length === 0) {
    return <p style={{ marginTop: "20px", fontStyle: "italic" }}>데이터가 없습니다.</p>;
  }
  
  return (
    <div className="protest-container">
      {filtered.map((item, idx) => {
        const {
          staff,
          date,
          time,
          location,
          happened_before,
          behaviors,
          function: func,
          consequences,
          duration,
        } = item;

        // behaviors에서 값이 있는 항목만 필터링
        const behaviorList = Object.entries(behaviors || {}).filter(
          ([_, value]) => value !== null
        );

        return (
          <div className="protest-card" key={idx}>
            <h4>🧾 {date || "날짜 없음"} {time || ""}</h4>
            <p><strong>👤 담당:</strong> {staff || "-"}</p>
            <p><strong>📍 위치:</strong> {location || "-"}</p>
            <p><strong>⚙ 발생 전:</strong> {happened_before || "-"}</p>
            <p><strong>⏱️ 소요시간:</strong> {duration?.min || 0}분 {duration?.sec || 0}초</p>
            <p><strong>📈 기능:</strong> {func || "-"}</p>

            {behaviorList.length > 0 && (
              <div>
                <strong>📌 행동:</strong>
                <ul className="behavior-list">
                  {behaviorList.map(([key, value]) => (
                    <li key={key}>{key.replace(/_/g, " ")}: {value}</li>
                  ))}
                </ul>
              </div>
            )}

            <p><strong>🎯 결과:</strong> {consequences || "-"}</p>
          </div>
        );
      })}
    </div>
  );
};

export default ProtestCard;
