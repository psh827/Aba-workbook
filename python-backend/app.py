# python-backend/app.py
from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import pandas as pd
import json
from bx_logic import extract_from_bx_excel
from programs_logic import extract_from_programs_excel
from short_long_logic import extract_from_short_long_protests_excel
from datetime import datetime
import traceback 

app = Flask(__name__)
CORS(app, resources={r"/upload": {"origins": "*"}})


UPLOAD_FOLDER = "uploads"
RULE_PATH = ["data/bx_info.json", "data/programs_info.json", "data/short_protest_info.json", "data/long_protest_info.json"]
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# ✅ 모든 시트를 순회해서 시트별 JSON 파일 생성
def convert_excel_to_multiple_json(excel_path, base_output_dir):
    xls = pd.ExcelFile(excel_path)

    # 폴더 이름: 원본 파일명_타임스탬프
    base_filename = os.path.splitext(os.path.basename(excel_path))[0]
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    folder_name = f"{base_filename}_{timestamp}"
    folder_path = os.path.join(base_output_dir, folder_name)
    os.makedirs(folder_path, exist_ok=True)

    for sheet_name in xls.sheet_names:
        df = pd.read_excel(xls, sheet_name=sheet_name)
        sheet_data = []

        for index, row in df.iterrows():
            row_dict = row.to_dict()
            row_items = []
            for key, value in row_dict.items():
                if pd.notna(value):
                    row_items.append([str(key), str(value)])
            sheet_data.append([index, row_items])

        safe_sheet_name = sheet_name.replace(" ", "_").replace("/", "_")
        output_filename = f"output_sheet_{safe_sheet_name}.json"
        output_path = os.path.join(folder_path, output_filename)

        with open(output_path, "w", encoding="utf-8") as f:
            json.dump(sheet_data, f, ensure_ascii=False, indent=4)

    # 업로드된 엑셀 파일도 해당 폴더로 이동
    saved_excel_path = os.path.join(folder_path, os.path.basename(excel_path))
    os.rename(excel_path, saved_excel_path)

    return folder_name, folder_path
#"data/bx_info.json"
# ✅ 업로드 API
@app.route("/upload", methods=["POST"])
def upload_file():
    bx_info_results = {}
    if 'file' not in request.files:
        return jsonify({"error": "파일이 포함되어야 합니다."}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "파일명이 비어있습니다."}), 400

    save_path = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(save_path)

    try:
        folder_name, folder_path = convert_excel_to_multiple_json(save_path, UPLOAD_FOLDER)
        bx_info_results = extract_from_bx_excel(RULE_PATH[0], os.path.join(folder_path, "output_sheet_bx.json"))
        programs_results = extract_from_programs_excel(RULE_PATH[1], os.path.join(folder_path, "output_sheet_programs.json"))
        protests = extract_from_short_long_protests_excel(RULE_PATH[2], RULE_PATH[3], os.path.join(folder_path, "output_sheet_sP@h.json"), os.path.join(folder_path, "output_sheet_lP@h.json"))
        print(bx_info_results)
        return jsonify({"message": "✅ 시트별 JSON 생성 완료", "folder": folder_name, "bx_info_results": bx_info_results, "programs_result": programs_results, "protests_results": protests})
    except Exception as e:
        traceback_str = traceback.format_exc()
        print("🔴 에러 발생:\n", traceback_str)
        return jsonify({"error": f"변환 실패: {str(e)}"}), 500

@app.route('/')
def index():
    return "✅ Flask 서버 작동 중입니다!"


if __name__ == '__main__':
    app.run(debug=True, port=5000)
