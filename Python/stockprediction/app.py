from flask import Flask, render_template, jsonify
from flask_cors import CORS
import subprocess

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

@app.route("/")
def index():
    return render_template("index.html")

@app.route("/api/stocks", methods=["GET"])
def get_stocks():
    stocks = ['GOOG', 'AAPL', 'MSFT', 'GME', 'AMZN', 'TSLA', 'FB', 'NFLX', 'NVDA']
    return jsonify(stocks)

@app.route("/streamlit")
def run_streamlit():
    # Start the Streamlit app as a separate process
    subprocess.run(["streamlit", "run", "streamlit_script.py"])

    # Return an empty response
    return ""

@app.route("/dashboard")
def run_dashboard():
    # Start the Streamlit app as a separate process
    subprocess.run(["streamlit", "run", "dashboard.py"])

    # Return an empty response
    return ""

if __name__ == "__main__":
    app.run(debug=True)
