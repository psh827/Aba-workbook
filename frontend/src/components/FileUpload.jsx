import React, { useState } from "react";
import './TableStyles.css';
/* BX */
import BXBasicInfo from "./bx/BXBasincInfo";
import BXProtestSection from "./bx/BXProtestSection";
import BXFunctionSummary from "./bx/BXFunctionSummary";
import BXTrackingData from "./bx/BXTrackingData";
import BXTreatmentInfoSection from "./bx/BXTreatmentInfoSection";
/* Programs_result */
import ProgramsSection from "./programs_result/ProgramsSection";
import ReinforcementSamplingSection from "./programs_result/ReinforcementSamplingSection";
import PromptingHierarchySection from "./programs_result/PromptingHierarchySection";
import PromptingHierarchy2Section from "./programs_result/PromptingHierarchy2Section";
/* Protests */
import ProtestCard from "./protests/ProtestCard";


const FileUpload = () => {
  const [file, setFile] = useState(null);
  const [response, setResponse] = useState(null);
  const [view, setView] = useState(null);        // 메인 선택 상태
  const [subView, setSubView] = useState(null);  // protests 내부 상태
  const 기능키 = ["escape", "access", "attention", "alone/play"];
  const programs_result_section_list = ['programs', 'reinforcement_samplings', 'prompting_hierarchy', 'prompting_hierarchy2'];
  const functionCounts = {};
  const handleChange = (e) => setFile(e.target.files[0]);

  const handleUpload = async () => {
    if (!file) return alert("파일을 선택하세요.");

    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await fetch("http://127.0.0.1:5000/upload", {
        method: "POST",
        body: formData,
      });

      const result = await res.json();
      setResponse(result);
      setView(null);
      setSubView(null);
    } catch (err) {
      console.error("업로드 실패:", err);
    }
  };

  const renderTable = (data, section) => {
    if (!data) return <p>데이터 없음</p>;

    if (programs_result_section_list.includes(section)) {
      switch (section) {
        case "programs":
          return <ProgramsSection data={data} />;
        case "reinforcement_samplings":
          return <ReinforcementSamplingSection data={data} />;
        case "prompting_hierarchy":
          return <PromptingHierarchySection data={data} />;
        case "prompting_hierarchy2":
          return <PromptingHierarchy2Section data={data} />;
        default:
          return <p>알 수 없는 프로그램 섹션입니다.</p>;
      }
    }

    if (section === "short_protests" || section === "long_protests") {
      return <ProtestCard data={data} />;
    }
    

    if (Array.isArray(data)) {
      if (data.length === 0) return <p>항목이 없습니다.</p>;

      const allKeys = Array.from(
        new Set(data.flatMap((item) => Object.keys(item)))
      );
      return (
        <table border="1" cellPadding="8" style={{ borderCollapse: "collapse", marginTop: "10px" }}>
          <thead>
            <tr>{allKeys.map((key) => <th key={key}>{key}</th>)}</tr>
          </thead>
          <tbody>
            {data.map((item, idx) => (
              <tr key={idx}>
                {allKeys.map((key) => (
                  <td key={key}>{JSON.stringify(item[key])}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      );
    }
    // 단일 객체 BXTreatmentInfo.jsx
    return (
      //bx_info
      <div>
        {section === "basic_info" && <BXBasicInfo data={data} />}
        {["short_protests", "long_protests"].map((key) => (
          <BXProtestSection key={key} data={data} type={key} functionCounts={functionCounts} />
        ))}
        {section === "home_protests" && (
          <>
            <BXFunctionSummary functionCounts={functionCounts} />
          </>
        )}
        {section === "tracking_data" && <BXTrackingData data={data} />}
        {section === "treatment_information" && (
          <BXTreatmentInfoSection data={data} />
        )}
      </div>
    );
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>엑셀 파일 업로드</h2>
      <input type="file" accept=".xlsx,.xls" onChange={handleChange} />
      <button onClick={handleUpload}>업로드</button>

      {response && (
        <div style={{ marginTop: "20px" }}>
          <p><strong>결과:</strong> ✅ {response.message}</p>
          <p><strong>폴더명:</strong> {response.folder}</p>

          {/* 🔘 메인 메뉴 */}
          <div style={{ marginTop: "20px" }}>
            <button onClick={() => setView("bx")}>📊 BX 데이터 보기</button>
            <button onClick={() => setView("programs")}>📘 프로그램 보기</button>
            <button onClick={() => { setView("protests"); setSubView(null); }}>📄 BX 기록지 보기</button>
          </div>

          {/* 🔍 BX 데이터 전체 출력 */}
          {view === "bx" &&
            Object.entries(response.bx_info_results || {}).map(([section, data]) => (
              <div key={section} style={{ marginTop: "30px" }}>
                <h3>📂 {section}</h3>
                {renderTable(data, section)}
              </div>
            ))
          }

          {/* 🔍 프로그램 데이터 전체 출력 */}
          {view === "programs" &&
              programs_result_section_list.map((section) => {
                const data = response.programs_result?.[section];
                if (!data) return null;
                return (
                  <div key={section} style={{ marginTop: "30px" }}>
                    <h3>📂 {section}</h3>
                    {renderTable(data, section)}
                  </div>
                );
              })
            }

          {/* 🔍 시위 데이터 보기 */}
          {view === "protests" && (
            <div style={{ marginTop: "20px" }}>
              <button onClick={() => setSubView("short")}> Short BX</button>
              <button onClick={() => setSubView("long")}> Long BX</button>

              {subView === "short" && renderTable(response.protests_results?.short_protests, "short_protests")}
              {subView === "long" && renderTable(response.protests_results?.long_protests, "long_protests")}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default FileUpload;
