import json

# ✅ 셀 값 추출 함수 (문자열인지 확인)
def get_cell(data, row, col):
    if not isinstance(col, str):
        return None
    for item in data:
        if item[0] == row:
            for col_name, value in item[1]:
                if col_name.strip() == col.strip():
                    return value
    return None

# ✅ 기본 정보 추출
def extract_basic_info(data, rules):
    return {key: get_cell(data, rule["row"], rule["col"]) for key, rule in rules.items()}

# ✅ 치료 정보 추출 (columns 안에 row + 하위컬럼 존재)
def extract_treatment_info(data, rules):
    result = {}
    for key, info in rules["columns"].items():
        row = info.get("row")
        result[key] = {
            subkey: get_cell(data, row, col)
            for subkey, col in info.items() if subkey != "row"
        }
    return result

# ✅ 트래킹 데이터 추출 (columns 안에 row + 하위컬럼 존재)
def extract_tracking_data(data, rules):
    result = {}
    for key, info in rules["columns"].items():
        row = info.get("row")
        result[key] = {
            subkey: get_cell(data, row, col)
            for subkey, col in info.items() if subkey != "row"
        }
    return result

# ✅ 시위 데이터 추출 (columns 안에 여러 key & subkey)
def extract_protests(data, rules):
    result = {}
    for category, items in rules["columns"].items():
        result[category] = {}
        for key, info in items.items():
            row = info.get("row")
            result[category][key] = {
                subkey: get_cell(data, row, col)
                for subkey, col in info.items() if subkey != "row"
            }
    return result

def extract_from_bx_excel(bx_info, output_file):
    with open(output_file, "r", encoding="utf-8") as f:
        data = json.load(f)

    with open(bx_info, "r", encoding="utf-8") as f:
        rules = json.load(f)

    result = {}
    result["basic_info"] = extract_basic_info(data, rules["basic_info"])
    result["tracking_data"] = extract_tracking_data(data, rules["tracking_data"])
    result["treatment_information"] = extract_treatment_info(data, rules["treatment_information"])
    result["home_protests"] = extract_protests(data, rules["home_protests"])

    return result