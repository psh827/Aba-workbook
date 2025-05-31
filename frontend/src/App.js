// frontend/src/App.jsx
import React from "react";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FileUpload from "./components/FileUpload";
import LoginPage from "./pages/LoginPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/upload" element={<FileUpload />} />
      </Routes>
    </Router>
  );
}

export default App;
