"""
run_eval.py — runs every question in questions.json against your running
backend, shows you the generated SQL + results, and lets you grade it.

Why manual grading instead of comparing SQL strings automatically:
two different SQL queries can be equally correct (e.g. "SELECT name FROM
products ORDER BY price DESC LIMIT 1" vs a version with an explicit WHERE
clause that does the same thing) — comparing exact result rows would be
more rigorous, but for a 2-day prototype, eyeballing "did this answer the
question correctly" is honest and fast. Note this tradeoff in your README;
it's a legitimate thing to mention in an interview ("here's what I'd improve
with more time" is a strong answer, not a weak one).

Usage:
    python3 run_eval.py
Make sure the Spring Boot backend is running on localhost:8080 first.
"""

import json
import requests

API_URL = "http://localhost:8080/api/query"
QUESTIONS_FILE = "questions.json"


def main():
    with open(QUESTIONS_FILE) as f:
        questions = json.load(f)

    results = []
    for i, item in enumerate(questions, 1):
        question = item["question"]
        expected_sql = item["expected_sql"]

        print(f"\n[{i}/{len(questions)}] {question}")
        print(f"Expected SQL hint: {expected_sql}")

        try:
            resp = requests.post(API_URL, json={"question": question}, timeout=30)
            data = resp.json()
        except requests.exceptions.RequestException as e:
            print(f"  Request failed: {e}")
            results.append({"question": question, "correct": False, "note": "request failed"})
            continue

        if data.get("error"):
            print(f"  Generated SQL: {data.get('generatedSql')}")
            print(f"  ERROR: {data['error']}")
            grade = input("  Mark correct anyway? (rare — y/N): ").strip().lower()
        else:
            print(f"  Generated SQL: {data.get('generatedSql')}")
            print(f"  Result rows: {data.get('rows')}")
            grade = input("  Correct? (y/N): ").strip().lower()

        results.append({"question": question, "correct": grade == "y"})

    correct_count = sum(1 for r in results if r["correct"])
    total = len(results)
    print(f"\n{'='*40}")
    print(f"Accuracy: {correct_count}/{total} ({100 * correct_count / total:.0f}%)")
    print(f"{'='*40}")

    with open("eval_results.json", "w") as f:
        json.dump(results, f, indent=2)
    print("Saved detailed results to eval_results.json")


if __name__ == "__main__":
    main()
