import { NavLink } from "react-router-dom";

const tabStyle = ({ isActive }) => ({
  padding: "10px 20px",
  marginRight: "10px",
  borderBottom: isActive ? "3px solid blue" : "1px solid gray",
  fontWeight: isActive ? "bold" : "normal",
  textDecoration: "none",
});

export default function TrackingTabs() {
  return (
    <div style={{ marginBottom: "20px", marginTop: "20px" }}>
      <NavLink to="/view/tracking/week" style={tabStyle}>
        주별 비교
      </NavLink>
      <NavLink to="/view/tracking/month" style={tabStyle}>
        월별 비교
      </NavLink>
      <NavLink to="/view/tracking/year" style={tabStyle}>
        연도별 비교
      </NavLink>
    </div>
  );
}
