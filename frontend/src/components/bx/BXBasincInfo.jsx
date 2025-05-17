import React from "react";

const BXBasicInfo = ({ data }) => {
  if (!data || !data.name) return null;

  return (
    <div>
      <h2>기본 정보</h2>
      <p>이름 : {data.name?.trim()}</p>
      <p>리더 : {data.leader_name}</p>
      <p>날짜 : {data.home_period}</p>
    </div>
  );
};

export default BXBasicInfo;
