import protestKeyMap from "../components/protestKeyMap";
export const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const { name, value } = payload[0];
      const label = protestKeyMap[name]?.[0] || name;
  
      return (
        <div
          style={{
            background: "white",
            border: "1px solid #ccc",
            padding: "8px",
            borderRadius: "4px",
            fontSize: "13px"
          }}
        >
          <strong>{label}</strong>: {value}
        </div>
      );
    }
    return null;
};

export const renderCustomLegend = (props) => {
    const { payload } = props;
    return (
      <div style={{
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "center",
        gap: "12px 20px", // 항목 간 간격
        paddingTop: "16px"
      }}>
        {payload.map((entry, index) => (
          <div key={`item-${index}`} style={{ display: "flex", alignItems: "center", fontSize: "13px" }}>
            <div style={{
              width: 10,
              height: 10,
              backgroundColor: entry.color,
              marginRight: 6,
              borderRadius: "50%"
            }} />
            <span>{protestKeyMap[entry.value]?.[0] || entry.value}</span>
          </div>
        ))}
      </div>
    );
};