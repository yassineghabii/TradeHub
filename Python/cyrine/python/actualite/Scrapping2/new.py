import requests
from bs4 import BeautifulSoup
from flask import Flask, render_template, request, redirect, url_for
from flask_cors import CORS

app = Flask(__name__)
CORS(app)




# Fonction pour extraire les articles du site
def scrape_articles(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    articles = []
    
    for article in soup.find_all('a', href=True):
        if "/marches/" in article['href']:
            articles.append({
                'title': article.text,
                'link': 'https://www.ilboursa.com' + article['href']
            })
    
    return articles

import requests
from bs4 import BeautifulSoup
CORS(app)
def scrape_convertisseur_devises(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    conversions = []

    # Trouver les éléments contenant les informations de conversion
    for row in soup.select('table.table-devises tr'):
        columns = row.find_all('td')
        if len(columns) >= 3:
            conversion = {
                'currency_from': columns[0].text.strip(),
                'currency_to': columns[1].text.strip(),
                'rate': columns[2].text.strip()
            }
            conversions.append(conversion)

    return conversions

# Exemple d'utilisation
url_convertisseur = 'https://www.ilboursa.com/marches/convertisseur_devises'
result = scrape_convertisseur_devises(url_convertisseur)

for conversion in result:
    print(conversion)

from flask import Flask, render_template, send_from_directory
from flask_cors import CORS
CORS(app)
@app.route('/i/<path:filename>', methods=['GET'])
def serve_static(filename):
    return send_from_directory('i', filename, mimetype='image/*')
from flask_cors import CORS

CORS(app)
@app.route('/scripts2/<path:filename>')
def serve_static_images(filename):
    script_dir = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'scripts2')
    return send_from_directory(script_dir, filename,mimetype='application/javascript')

from flask_cors import CORS

CORS(app)
@app.route('/5/flag/<path:filename>', methods=['GET'])
def serve_static_flag(filename):
    script_dir = os.path.join(os.path.dirname(os.path.realpath(__file__)), '5/flag')
    return send_from_directory(script_dir, filename, mimetype='image/*')
from flask_cors import CORS
CORS(app)
@app.route('/css2/<path:filename>', methods=['GET'])
def serve_static_css(filename):
    css_dir = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'css2')
    return send_from_directory(css_dir, filename, mimetype='text/css')

from flask_cors import CORS
CORS(app)
@app.route('/marches/actualites_bourse_tunis', methods=['GET'])
def actualites_bourse_tunis():
    url = "https://www.ilboursa.com/marches/actualites_bourse_tunis"
    articles = scrape_articles(url)
    return render_template('actualites_new.html', articles=articles,  mimetype='text/html')


from flask_cors import CORS
CORS(app)
@app.route('/marches/convertisseur_devises',methods=['GET'])
def convertisseur_devises():
    url = "https://www.ilboursa.com/marches/convertisseur_devises"
    conversions = scrape_convertisseur_devises(url)
    return render_template('convertisseur_devises.html', conversions=conversions)
from flask_cors import CORS


CORS(app)
@app.route('/', methods=['GET'])
def index():
    articles = scrape_articles("https://www.ilboursa.com/marches/actualites_bourse_tunis")
    return render_template('indexx.html', articles=articles)
from flask_cors import CORS


CORS(app)
@app.route('/marches/<path:article_name>', methods=['GET'])
def article(article_name):
    # Récupérer l'article complet à partir de l'URL
    article_url = f"https://www.ilboursa.com/marches/{article_name}"
    article_content = scrape_article_content(article_url)
    
    if article_content:
        return render_template('articles.html', article_content=article_content)
    else:
        return "Article non trouvé ou erreur de scraping."
# Fonction pour extraire le contenu de l'article à partir de l'URL de l'article
CORS(app)
def scrape_article_content(article_url):
    response = requests.get(article_url)
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        article_content = str(soup.find(id="containerPage"))
        return article_content
    else:
        return None




@app.route('/experts', methods=['GET'])
def scrape_and_display_expert():
    # URL of the website to be scraped
    website_url = "https://www.ilboursa.com/analyses/experts_de_leco"
    
    # Scraping the HTML content of the website
    website_content = scrape_website_content(website_url)

    if website_content:
        # Save the HTML content to expert.html in the templates folder
        save_to_file(website_content, "templates/expert.html")
        
        # Display the HTML content
        return render_template('experts.html', website_content=website_content)
    else:
        return "Erreur de scraping."

@app.route('/analyses/<path:article_name>', methods=['GET'])
def display_article(article_name):
    # Construct the full URL of the article
    article_url = f"https://www.ilboursa.com/analyses/{article_name}"
    
    # Scraping the HTML content of the article
    article_content, image_url = scrape_article_contents(article_url)

    if article_content:
        # Display the HTML content of the article along with the image URL
        return render_template('eco.html', article_content=article_content, image_url=image_url)
    else:
        return "Erreur de scraping."


def scrape_website_content(website_url):
    response = requests.get(website_url)
    
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        website_content = str(soup)
        return website_content
    else:
        return None

def scrape_article_contents(article_url):
    response = requests.get(article_url)
    
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')

        # Extract article content and image URL
        article_content = str(soup.find(id="containerPage"))
        image_url = soup.find('img')['src'] if soup.find('img') else None

        # Add the website prefix to the image URL
        if image_url and not image_url.startswith("http"):
            image_url = "https://www.ilboursa.com/" + image_url

        return article_content, image_url
    else:
        return None, None

def save_to_file(content, file_path):
    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)

from flask import send_file
import io
@app.route('/images/<path:image_name>', methods=['GET'])
def serve_image(image_name):
    # Construct the full URL of the image
    image_url = f"https://www.ilboursa.com/handlers/image_news_get.ashx?id={image_name}"

    # Fetch the image content
    response = requests.get(image_url)

    if response.status_code == 200:
        # Serve the image locally
        return send_file(io.BytesIO(response.content), mimetype='image/jpeg')
    else:
        # Return a default image or handle the error as needed
        return send_file('path/to/default-image.jpg', mimetype='image/jpeg')

CORS(app)

def scrape_conseil(url):
    response = requests.get(url)
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        conseil_content = str(soup.find(id="containerPage"))
        return conseil_content
    else:
        return None
CORS(app)

def write_to_file(content, file_path):
    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)

from flask_cors import CORS
CORS(app)
@app.route('/conseils', methods=['GET'])
def conseils():
    url = "https://www.ilboursa.com/analyses/conseil/PX1"
    conseil_content = scrape_conseil(url)
    
    if conseil_content:
        return render_template('conseils.html', conseil_content=conseil_content)
    else:
        return "Contenu des conseils non trouvé ou erreur de scraping."
    
import os
CORS(app)

def scrape_synthese_fiches(url):
    response = requests.get(url)
    if response.status_code == 200:
        return response.text
    else:
        return None

from flask_cors import CORS
CORS(app)
@app.route('/synthese_fiches', methods=['GET'])
def synthese_fiches():
    url = "https://www.ilboursa.com/analyses/synthese_fiches"
    synthese_fiches_content = scrape_synthese_fiches(url)
    
    if synthese_fiches_content:
        return render_template('synthese_fiches.html', synthese_fiches_content=synthese_fiches_content)
    else:
        return "Contenu de la synthèse des fiches non trouvé ou erreur de scraping."

CORS(app)

def scrape_and_save_consensus(url):
    response = requests.get(url)
    if response.status_code == 200:
        return response.text
    else:
        return None
from flask_cors import CORS
CORS(app)

@app.route('/analyses/consensus', methods=['GET'])
def consensus():
    url = "https://www.ilboursa.com/analyses/consensus"
    consensus_content = scrape_and_save_consensus(url)
    
    if consensus_content:
        return render_template('consensus.html', consensus_content=consensus_content)
    else:
        return "Contenu du consensus non trouvé ou erreur de scraping."
CORS(app)

def scrape_and_save_synthese_tendance(url):
    response = requests.get(url)
    if response.status_code == 200:
        return response.text
    else:
        return None
from flask_cors import CORS
CORS(app)

@app.route('/synthese_tendance', methods=['GET'])
def synthese_tendance():
    url = "https://www.ilboursa.com/analyses/synthese_tendance"
    synthese_tendance_content = scrape_and_save_synthese_tendance(url)
    
    if synthese_tendance_content:
        return render_template('synthese_tendance.html', synthese_tendance_content=synthese_tendance_content)
    else:
        return "Contenu de la synthèse tendance non trouvé ou erreur de scraping."
# Import des modules nécessaires
from bs4 import BeautifulSoup
import requests
CORS(app)

# URL de la page synthese_fiches
url_synthese_fiches = "https://www.ilboursa.com/analyses/synthese_fiches"

# Récupération du contenu de la page
response = requests.get(url_synthese_fiches)
soup = BeautifulSoup(response.text, 'html.parser')

# Extraction des liens
liens_conseils = []

for td in soup.select('td > a[href^="conseil/"]'):
    valeur = td['href'].split('/')[1]
    nom_societe = td.text
    liens_conseils.append({
        'valeur': valeur,
        'nom_societe': nom_societe
    })

# Affichage des résultats
for lien in liens_conseils:
    print(f"Valeur: {lien['valeur']}, Nom de la société: {lien['nom_societe']}")








from flask_cors import CORS
CORS(app)
# Ajoutez cette route pour gérer les conseils individuels
@app.route('/conseil/<valeur>', methods=['GET'])
def conseil_individuel(valeur):
    url = f"https://www.ilboursa.com/analyses/conseil/{valeur}"
    conseil_content = scrape_conseil(url)
    
    if conseil_content:
        return render_template('conseil_individuel.html', conseil_content=conseil_content)
    else:
        return "Contenu du conseil non trouvé ou erreur de scraping."
    

CORS(app)
def scrape_economie(url, output_file="templates/economie_content.html"):
    response = requests.get(url)
    if response.status_code == 200:
        content = response.text
        write_to_file(content, output_file)
        return content
    else:
        return None
def scrape_and_save_secteur(url, output_file="templates/secteur.html"):
    response = requests.get(url)
    if response.status_code == 200:
        content = response.text
        write_to_file(content, output_file)
        return content
    else:
        return None

# Fonction pour écrire dans un fichier
def write_to_file(content, output_file):
    with open(output_file, 'w', encoding='utf-8') as file:
        file.write(content)

# Route pour le secteur (accepte les méthodes GET et POST)
@app.route('/secteur', methods=['GET', 'POST'])
def secteur():
    if request.method == 'POST':
        selected_sector = request.form.get('dlSector')
        # Faites quelque chose avec la valeur sélectionnée si nécessaire
        print(f"Secteur sélectionné : {selected_sector}")

    url = "https://www.ilboursa.com/marches/secteurs"
    secteur_content = scrape_and_save_secteur(url)

    if secteur_content:
        return render_template('secteur.html', secteur_content=secteur_content)
    else:
        return "Contenu du secteur non trouvé ou erreur de scraping."
# ...
from flask_cors import CORS
CORS(app)
@app.route('/economie', methods=['GET'])
def economie():
    url = "https://www.ilboursa.com/economie/"
    economie_content = scrape_economie(url)
    
    if economie_content:
        return render_template('economie.html', economie_content=economie_content)
    else:
        return "Contenu de la section Économie non trouvé ou erreur de scraping."
    

    

@app.route('/economi', methods=['GET'])
def economi():
    url = "https://www.ilboursa.com/economie/"  # Replace with the actual URL
    economie_content = scrape_economie(url)
    
    if economie_content:
        return render_template('economie_content.html', economie_content=economie_content)
    else:
        return "Contenu de l'économie non trouvé ou erreur de scraping."

CORS(app)

def scrape_actualites(url, output_file="templates/actualites_new.html"):
    response = requests.get(url)
    if response.status_code == 200:
        content = response.text
        write_to_file(content, output_file)
        return content
    else:
        return None

CORS(app)

def write_to_file(content, file_path):
    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)

from flask_cors import CORS

CORS(app)

@app.route('/actualites', methods=['GET'])
def actualites():
    url = "https://www.ilboursa.com/marches/actualites_bourse_tunis"
    actualites_content = scrape_actualites(url)
    
    if actualites_content:
        return render_template('actualites_new.html', actualites_content=actualites_content)
    else:
        return "Contenu des actualités non trouvé ou erreur de scraping."

    
import subprocess
from pathlib import Path

@app.route('/actua')
def actua():
    script_path = Path("C:/Users/ASUS TUF I5/Desktop/Vermeg/Scrapping2/new.py")
    python_interpreter ="C:/Users/ASUS TUF I5/AppData/Local/Programs/Python/Python310/python.exe" # Mettez à jour le chemin avec votre interpréteur Python

    subprocess.run([str(python_interpreter), str(script_path)])
    return ""
    



if __name__ == '__main__':
    app.run(port=5002)
