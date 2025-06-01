import json
import pandas as pd

# ✅ 셀 값 추출 함수
def get_cell(data, row, col):
    for item in data:
        if item[0] == row:
            for c, v in item[1]:
                if c.strip() == col.strip():
                    return v
    return None

# ✅ 유지관리 비율 항목 추출
def extract_maintenance_info(info, data):
    result = []
    for block in info:
        item = {
            "item_text": get_cell(data, block["item_text"]["row"], block["item_text"]["col"]),
            "ratios": {
                "first": {
                    "ratios_text": get_cell(data, block["first_row"]["ratios_text"]["row"], block["first_row"]["ratios_text"]["col"]),
                    "ratios_suc": get_cell(data, block["first_row"]["ratios_suc"]["row"], block["first_row"]["ratios_suc"]["col"]),
                    "ratios_attemp": get_cell(data, block["first_row"]["ratios_attemp"]["row"], block["first_row"]["ratios_attemp"]["col"]),
                    "calculate": get_cell(data, block["first_row"]["calculate"]["row"], block["first_row"]["calculate"]["col"]),
                    "note": get_cell(data, block["first_row"]["note"]["row"], block["first_row"]["note"]["col"])
                },
                "second": {
                    "ratios_text": get_cell(data, block["second_row"]["ratios_text"]["row"], block["second_row"]["ratios_text"]["col"]),
                    "ratios_suc": get_cell(data, block["second_row"]["ratios_suc"]["row"], block["second_row"]["ratios_suc"]["col"]),
                    "ratios_attemp": get_cell(data, block["second_row"]["ratios_attemp"]["row"], block["second_row"]["ratios_attemp"]["col"]),
                    "calculate": get_cell(data, block["second_row"]["calculate"]["row"], block["second_row"]["calculate"]["col"]),
                    "note": get_cell(data, block["second_row"]["note"]["row"], block["second_row"]["note"]["col"])
                }
            }
        }
        result.append(item)
    
    return result

def extract_from_maintenance_excel(maintenance_info, maintenance_output_file):
    with open(maintenance_info, "r", encoding="utf-8") as f:
        maintenance_rules = json.load(f)

    with open(maintenance_output_file, "r", encoding="utf-8") as f:
        maintenance_data = json.load(f)

    result = {}
    result["maintenance"] = extract_maintenance_info(maintenance_rules["maintenance_items"], maintenance_data)

    return result