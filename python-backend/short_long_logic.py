import json

# ✅ 셀 값 추출 함수
def get_cell(data, row, col_name):
    for entry in data:
        if entry[0] == row:
            for col, val in entry[1]:
                if col.strip() == col_name.strip():
                    return val
    return None

# ✅ 단기 문제행동 데이터 추출
def extract_protests(data, info, gubun):
    result = []
    gubun_str = "short_protests" if gubun == "short" else "long_protests"

    for item in info[gubun_str]:
        row = item["row"]
        entry = {
            "row": row,
            "staff": get_cell(data, row, item["staff"]),
            "date": get_cell(data, row, item["date"]),
            "time": get_cell(data, row, item["time"]),
            "location": get_cell(data, row, item["location"]),
            "happened_before": get_cell(data, row, item["happened_before"]),
            "duration": {
                "min": get_cell(data, row, item["duration"]["min"]),
                "sec": get_cell(data, row, item["duration"]["sec"]),
            },
            "function": get_cell(data, row, item["function"]),
            "consequences": get_cell(data, row, item["consequences"])
        }


        # ✅ 문제행동 항목들 (true/false 또는 수치 포함)
        behaviors = [
            "cry", "cry_w_tears", "drop", "scream", "whine",
            "hit_ppl", "hit_obj", "hit_self", "kick_ppl", "kick_obj",
            "throw", "throw_obj", "scratch_ppl", "scratch_self",
            "bite_ppl", "bite_obj", "bite_self", "elope",
            "laughed_uncontrollably", "etc"
        ]
        behaviors_tmp = {}
        for behavior in behaviors:
            behaviors_tmp[behavior] = get_cell(data, row, item[behavior])

        entry["behaviors"] = behaviors_tmp
        
         # 🧠 핵심 로직: happened_before만 있고 나머지 주요 데이터가 없는 경우
        if (
            entry["happened_before"]
            and not any([entry["staff"], entry["date"], entry["time"], entry["location"]])
        ):
            if last_full_entry:
                last_full_entry["happened_before"] += f" {entry['happened_before']}"
        else:
            result.append(entry)
            last_full_entry = entry

    return result

def extract_from_short_long_protests_excel(short_protests_info, long_protests_info, short_output_file, long_output_file):
    with open(short_output_file, "r", encoding="utf-8") as f:
        short_data = json.load(f)

    with open(long_output_file, "r", encoding="utf-8") as f:
        long_data = json.load(f)

    with open(short_protests_info, "r", encoding="utf-8") as f:
        short_rules = json.load(f)

    with open(long_protests_info, "r", encoding="utf-8") as f:
        long_rules = json.load(f)

    result = {}
    result["short_protests"] = extract_protests(short_data, short_rules, "short")
    result["long_protests"] = extract_protests(long_data, long_rules, "long")

    return result