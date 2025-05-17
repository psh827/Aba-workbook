// frontend/src/App.jsx
import React from "react";
import FileUpload from "./components/FileUpload";

function App() {
  return (
    <div style={{ padding: "40px" }}>
      <h1>ABA Workbook</h1>
      <FileUpload />
    </div>
  );
}

export default App;
