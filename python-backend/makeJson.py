import json

def generate_treatment_items(start_rows, count_per_block=17):
    result = []

    for base in start_rows:
        for i in range(count_per_block):
            even_row = base + i * 2
            odd_row = even_row + 1
            block = {
                "item_text": {"row": even_row, "col": "Unnamed: 1"},
                "first_row": {
                    "ratios_text": {"row": even_row, "col": "Unnamed: 11"},
                    "ratios_suc": {"row": even_row, "col": "Unnamed: 14"},
                    "ratios_attemp": {"row": even_row, "col": "Unnamed: 15"},
                    "calculate": {"row": even_row, "col": "Unnamed: 16"},
                    "note": {"row": even_row, "col": "Unnamed: 19"}
                },
                "second_row": {
                    "ratios_text": {"row": odd_row, "col": "Unnamed: 11"},
                    "ratios_suc": {"row": odd_row, "col": "Unnamed: 17"},
                    "ratios_attemp": {"row": odd_row, "col": "Unnamed: 18"},
                    "calculate": {"row": odd_row, "col": "Unnamed: 16"},
                    "note": {"row": odd_row, "col": "Unnamed: 19"}
                }
            }
            result.append(block)

    return result

# 시작 지점: 4, 43, 83
start_rows = [4, 43, 83]
treatment_items = generate_treatment_items(start_rows)

# JSON 파일로 저장
with open("treatment_items.json", "w", encoding="utf-8") as f:
    json.dump(treatment_items, f, ensure_ascii=False, indent=2)

print("✅ treatment_items.json 파일이 생성되었습니다. 총 블록 수:", len(treatment_items))
