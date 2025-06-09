import { useEffect, useState } from "react";
//import { fetchTrackingWeekData } from "./api"; // view/api.js 내 함수
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

export default function TrackingWeek({ selectedBehavior }) {
  const [weekData, setWeekData] = useState([]);

  /*useEffect(() => {
    const fetchData = async () => {
      if (selectedBehavior) {
        try {
          const data = await fetchTrackingWeekData("KRDGC25001", selectedBehavior);
          setWeekData(data);
        } catch (err) {
          console.error("주별 데이터 가져오기 실패:", err);
        }
      }
    };
    fetchData();
  }, [selectedBehavior]);*/

  return (
    <div className="p-4">
      <h3 className="text-xl font-semibold mb-4">🗓️ 주별 비교: {selectedBehavior}</h3>
      {weekData.length > 0 ? (
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={weekData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="week" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="successCount" stroke="#8884d8" name="성공 횟수" />
            <Line type="monotone" dataKey="attemptCount" stroke="#82ca9d" name="시도 횟수" />
          </LineChart>
        </ResponsiveContainer>
      ) : (
        <p>📭 해당 행동에 대한 주별 데이터가 없습니다.</p>
      )}
    </div>
  );
}
