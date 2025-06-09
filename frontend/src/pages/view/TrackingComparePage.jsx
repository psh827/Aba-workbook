import { useState, useEffect } from "react";
import { getTrackingBehavior } from "../../services/view/api"; // API 호출 함수
import BehaviorSelectBox from "./BehaviorSelectBox";
import { useNavigate, useParams } from "react-router-dom";
import TrackingWeek from './Trackingweek';         // 상대 경로에 맞게 조정
import TrackingTabs from './TrackingTabs';         // 상대 경로에 맞게 조정

export default function TrackingComparePage() {
  const [behaviorList, setBehaviorList] = useState([]);
  const [selectedBehavior, setSelectedBehavior] = useState("");
  const { type } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBehaviorList = async () => {
      try {
        const res = await getTrackingBehavior("KRDGC25001"); // 임시 아동 ID
        if (Array.isArray(res.data)) {
            console.log(res)
          setBehaviorList(res.data);
          if (res.length > 0) setSelectedBehavior(res[0]); // 첫 번째를 기본값으로
        } else {
          console.error("behaviorList 응답 형식이 배열이 아닙니다.", res);
        }
      } catch (error) {
        console.error("behaviorList 가져오기 실패:", error);
      }
    };
    fetchBehaviorList();
  }, []);

  const handleChange = (e) => {
    setSelectedBehavior(e.target.value);
  };

  const renderContent = () => {
    const commonProps = { selectedBehavior }; // 공통 prop
    switch (type) {
      case "week":
        return <TrackingWeek {...commonProps} />;
      /*case "month":
        return <TrackingMonth {...commonProps} />;
      case "year":
        return <TrackingYear {...commonProps} />;*/
      default:
        return <div>유효하지 않은 비교 타입입니다.</div>;
    }
  };

  return (
    <div>
      <h2>트래킹 데이터 비교</h2>
      <label>행동 선택: </label>
      <BehaviorSelectBox
        behaviorList={behaviorList}
        selectedBehavior={selectedBehavior}
        onChange={setSelectedBehavior}
      />
      <TrackingTabs />
      {renderContent()}
    </div>
  );
}
