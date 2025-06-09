import React from "react";

const BehaviorSelectBox = ({ behaviorList, selectedBehavior, onChange }) => {
  return (
    <select value={selectedBehavior} onChange={(e) => onChange(e.target.value)}>
      <option value="">행동을 선택하세요</option>
      {behaviorList.map((b, idx) => (
        <option key={idx} value={b}>
        {b}
        </option>
     ))}
    </select>
  );
};

export default BehaviorSelectBox;
