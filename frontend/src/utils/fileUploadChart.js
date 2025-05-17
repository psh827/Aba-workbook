import { protestKeyMap } from "../components/protestKeyMap";
/* bx chart start */
export const generateLongProtestPieData = (longProtestsData) => {
    const 기능키 = ["escape", "access", "attention", "alone/play"];
    const pieData = [];
  
    Object.entries(longProtestsData).forEach(([key, raw]) => {
      if (
        raw?.action &&
        raw?.count &&
        raw?.count.trim() !== "" &&
        !기능키.includes(key) // "기능" 제외
      ) {
        pieData.push({
          name: key,
          value: Number(raw.count),
        });
      }
    });
  
    return pieData;
  };

/*
  원형 차트 색깔 순서
  1. bite_obj
  2. bite_ppl
  3. bite_self
  4. cry
  5. cry_with_teats
  6. drop
  7. elope
  8. etc
  9. hit_obj
  10. hit_ppl
  11. hit_self
  12. kick_obj
  13. kick_ppl
  14. NC
  15. scratch_ppl
  16. scratch_self
  17. scream
  18. throw_boj
  19. throw_to_ppl
  20. whine
*/
const COLORS = [
    /*1.분홍*/ "#F7CFD8",
    /*2.아이보리*/"#F4F8D3",
    /*3.하늘*/"#A6D6D6",
    /*4.보라*/"#8E7DBE",
    /*5.연두*/"#90C67C",
    /*6.다홍*/"#FFF1D5",
    /*7.주황*/"#0FFA955",
    /*8.노랑*/"#F8ED8C",
    /*9.파랑*/"#578FCA",
    /*10.베이지*/"#BFBBA9",
    /*11.회색*/"#9AA6B2",
    /*12.민트*/"#BEE4D0",
    /*13.*/"#9FB3DF",
    /*14.*/"#B0C4DE",
    /*15.*/"#FFDEAD",
    /*16.*/"#BDDDE4",
    /*17.*/"#FF6363",
    /*18.*/"#60B5FF",
    /*19.*/"#AFDDFF",
    /*20.*/"#FFECDB"
  ];
  
// ✅ 항목(key) 기준으로 색상 매핑 생성
export function generateColorMapFromData(data) {
    const colorMap = {};
    const allKeys = new Set();
  
    ["long_protests", "short_protests"].forEach(section => {
      const sectionData = data[section];
      if (!sectionData) return;
  
      Object.keys(sectionData).forEach(key => {
        if (![
          "this_week_cnt", "past_week_cnt",
          "this_week_avg_time", "past_week_avg_time",
          "function_of_bx_item1", "function_of_bx_item2",
          "function_of_bx_item3", "function_of_bx_item4",
          "total_ssn_cnt"
        ].includes(key)) {
          allKeys.add(key);
        }
      });
    });
  
    let i = 0;
    for (const key of allKeys) {
      colorMap[key] = COLORS[i % COLORS.length];
      i++;
    }
  
    return colorMap;
};

export const generateBarChartData = (data) => {
    return Object.entries(data)
      .filter(([key, value]) => {
        if (key === "non-responses") return false;
        const current = parseFloat(value.calculate);
        const past = parseFloat((value.result_past_week || "").replace("/ssn", "").replace("%", ""));
        return !isNaN(current) && !isNaN(past);
      })
      .map(([key, value]) => ({
        name: value.behavior,
        thisWeek: parseFloat(value.calculate),
        lastWeek: parseFloat((value.result_past_week || "").replace("/ssn", "").replace("%", ""))
      }));
};

/* bx chart end */