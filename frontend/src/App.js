// frontend/src/App.jsx
import React from "react";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FileUpload from "./components/FileUpload";
import LoginPage from "./pages/LoginPage";
import ViewPage from "./pages/ViewPage";
import TrackingComparePage from "./pages/view/TrackingComparePage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/upload" element={<FileUpload />} />
        <Route path="/view" element={<ViewPage />} />
        <Route path="/view/tracking/:type" element={<TrackingComparePage />} />
      </Routes>
    </Router>
  );
}

export default App;
