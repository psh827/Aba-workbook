/**
 * 시간포맷
 * EX: "00:02:18.710000" => "00:02:18"
 * @param {*} timeStr 
 * @returns 
 */
export const formatToHHMMSS = (timeStr) => {
    if (!timeStr) return "";
  
    // 초 단위까지 포함되어 있을 경우
    if (timeStr.includes(":")) {
      const parts = timeStr.split(":");
  
      // 예: "2:18" => ["2", "18"]
      if (parts.length === 2) {
        return `00:${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}`;
      }
  
      // 예: "00:02:18.710000" => "00:02:18"
      if (parts.length === 3) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}:${parts[2].slice(0, 2).padStart(2, "0")}`;
      }
    }
  
    return "00:00:00";
  };

  