import json


# ✅ 셀 값 추출 함수
def get_cell(data, row, col):
    for item in data:
        if item[0] == row:
            for c, v in item[1]:
                if c.strip() == col.strip():
                    return v
    return None

# ✅ 프로그램 데이터 추출
def extract_programs(info, data):
    results = {}
    for key, program in info["programs"].items():
        name_info = program.get("program_name", {})
        program_name = get_cell(data, name_info.get("row"), name_info.get("col"))

        items = []
        for item in program["items"]:
            item_val = get_cell(data, item["row"], item["item"])
            st_val = get_cell(data, item["row"], item["item_status"])
            tsm_val = get_cell(data, item["row"], item["item_tsm"])
            if item_val:
                items.append({
                    "item": item_val,
                    "item_status": st_val,
                    "tsm": tsm_val
                })

        note_info = program["notes"]["session_cnt"]
        session_cnt = get_cell(data, note_info["row"], note_info["col"])

        notes = []
        for note in program["notes"]["note_txts"]:
            note_val = get_cell(data, note["row"], note["col"])
            if note_val:
                notes.append(note_val)

        results[key] = {
            "program_name": program_name,
            "session_cnt": session_cnt,
            "items": items,
            "notes": notes
        }
    return results

# ✅ reinforcement_samplings 추출
def extract_reinforcement_samplings(entries, data):
    results = []
    for entry in entries:
        def get_nested(field):
            return get_cell(data, field["row"], field["col"])

        results.append({
            "title": get_nested(entry["title"]),
            "detail_sampling": get_nested(entry["detail_sampling"]),
            "sampling_info": {
                "session_cnt": get_nested(entry["sampling_info"]["session_cnt"]),
                "tsm": get_nested(entry["sampling_info"]["tsm"]),
                "MI": get_nested(entry["sampling_info"]["MI"])
            },
            "first_row": {
                "ratios_text": get_nested(entry["first_row"]["ratios_text"]),
                "ratios_suc": get_nested(entry["first_row"]["ratios_suc"]),
                "ratios_attempt": get_nested(entry["first_row"]["ratios_attemp"]),
                "note1": get_nested(entry["first_row"]["note1"]),
                "note2": get_nested(entry["first_row"]["note2"])
            },
            "second_row": {
                "ratios_text": get_nested(entry["second_row"]["ratios_text"]),
                "ratios_suc": get_nested(entry["second_row"]["ratios_suc"]),
                "ratios_attempt": get_nested(entry["second_row"]["ratios_attemp"]),
                "note1": get_nested(entry["second_row"]["note1"]),
                "note2": get_nested(entry["second_row"]["note2"])
            }
        })
    return results

# ✅ prompting_hierarchy 추출 (카테고리 자동 상속 포함)
def extract_prompting_hierarchy_with_inheritance(blocks, data):
    result = []
    last_category = None

    for block in blocks:
        category = get_cell(data, block["category"]["row"], block["category"]["col"])
        sub_category = get_cell(data, block["sub_category"]["row"], block["sub_category"]["col"])
        print(category)
        if category:
            last_category = category
        elif category == None and sub_category:
            category = last_category

        
        

        # 비율 정보
        ratios = {
            "first_ratio": {
                "text": get_cell(data, block["ratios"]["first_ratio"]["text"]["row"], block["ratios"]["first_ratio"]["text"]["col"]),
                "success_cnt": get_cell(data, block["ratios"]["first_ratio"]["success_cnt"]["row"], block["ratios"]["first_ratio"]["success_cnt"]["col"]),
                "attempt_cnt": get_cell(data, block["ratios"]["first_ratio"]["attemp_cnt"]["row"], block["ratios"]["first_ratio"]["attemp_cnt"]["col"])
            },
            "second_ratio": {
                "text": get_cell(data, block["ratios"]["second_ratio"]["text"]["row"], block["ratios"]["second_ratio"]["text"]["col"]),
                "success_cnt": get_cell(data, block["ratios"]["second_ratio"]["success_cnt"]["row"], block["ratios"]["second_ratio"]["success_cnt"]["col"]),
                "attempt_cnt": get_cell(data, block["ratios"]["second_ratio"]["attemp_cnt"]["row"], block["ratios"]["second_ratio"]["attemp_cnt"]["col"]),
                "session_cnt": get_cell(data, block["ratios"]["second_ratio"]["session_cnt"]["row"], block["ratios"]["second_ratio"]["session_cnt"]["col"]),
                "tsm": get_cell(data, block["ratios"]["second_ratio"]["tsm"]["row"], block["ratios"]["second_ratio"]["tsm"]["col"])
            }
        }

        activities = []
        for step in block["steps"]:
            step_info = {}
            for k, v in step.items():
                step_info[k] = get_cell(data, v["row"], v["col"])
            activities.append(step_info)

        result.append({
            "category": category,
            "sub_category": sub_category,
            "ratios": ratios,
            "activities": activities
        })

    return result

def extract_from_programs_excel(programs_info, programs_output_file):
    with open(programs_output_file, "r", encoding="utf-8") as f:
        programs_data = json.load(f)

    with open(programs_info, "r", encoding="utf-8") as f:
        programs_rules = json.load(f)

    result = {}
    result["programs"] = extract_programs(programs_rules, programs_data)
    result["reinforcement_samplings"] = extract_reinforcement_samplings(programs_rules["reinforcement_samplings"], programs_data)
    result["prompting_hierarchy"] = extract_prompting_hierarchy_with_inheritance(programs_rules["prompting_hierarchy"], programs_data)
    result["prompting_hierarchy2"] =  extract_prompting_hierarchy_with_inheritance(programs_rules["prompting_hierarchy2"], programs_data)
    return result