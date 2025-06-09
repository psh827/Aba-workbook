import axios from "../api";

// tracking behavior 조회
export const getTrackingBehavior = async () => {
  try {
    const response = await axios.get("/view/tracking", {
        params: {
        childId: "KRDGC25001", // 임시 하드코딩 값
      },
    });
    return response;
  } catch (error) {
    console.error("📛 getTrackingBehavior 실패:", error);
    throw error;
  }
};
