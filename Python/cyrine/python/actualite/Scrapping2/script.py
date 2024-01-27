from flask import Flask, render_template, request
import subprocess
from pathlib import Path

app = Flask(__name__)

import subprocess
from pathlib import Path

@app.route('/actua')
def actua():
    script_path = Path("C:/Users/ASUS TUF I5/Desktop/Vermeg/Scrapping2/new.py")
    python_interpreter ="C:/Users/ASUS TUF I5/AppData/Local/Programs/Python/Python310/python.exe" # Mettez à jour le chemin avec votre interpréteur Python

    subprocess.run([str(python_interpreter), str(script_path)])
    return ""

if __name__ == '__main__':
    app.run(debug=True, port=5003)
