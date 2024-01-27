import streamlit as st, pandas as pd, numpy as np, yfinance as yf
import plotly.express as px
from alpha_vantage.fundamentaldata import FundamentalData
import pandas_ta as ta
from pyChatGPT import ChatGPT
from stocknews import StockNews
import requests
from textblob import TextBlob
from bs4 import BeautifulSoup
import spacy
import talib
import websocket
import json

api_key = '08580393d090448282a4532bbaa1ce1c'
nlp = spacy.load("en_core_web_sm")


st.title('Stock Dashboard')
ticker = st.sidebar.text_input('Ticker')
start_date = st.sidebar.date_input('Start Date')
end_date = st.sidebar.date_input('End Date')

data = yf.download(ticker, start=start_date, end=end_date)
fig = px.line(data, x=data.index, y=data['Adj Close'], title = ticker)
st.plotly_chart(fig)

pricing_data, fundamental_data, tech_indicator, bott, news, openai1 = st.tabs(["Pricing Data", "Fundamental Data", "Technical Analysis", "Bitcoin Trading Bot", "Sentiment Analysis", "OpenAI ChatGPT"])

with pricing_data:
    st.header('Price Movements')
    data2 = data
    data2['% Change'] = data['Adj Close'] / data['Adj Close'].shift(1) - 1
    data2.dropna(inplace = True)
    st.write(data2)
    annual_return = data2['% Change'].mean()*252*100
    st.write('Annual Return is ', annual_return,'%')
    stdev = np.std(data2['% Change'])*np.sqrt(252)
    st.write('Standard Deviation is ',stdev*100,'%')
    st.write('Risk Adj Return is ',annual_return/(stdev*100),'%')



with fundamental_data:
    key = 'S2C2YZXMWVTOGCPB'
    fd = FundamentalData(key,output_format = 'pandas')
    st.subheader('Balance Sheet')
    balance_sheet = fd.get_balance_sheet_annual(ticker)[0]
    bs = balance_sheet.T[2:]
    bs.columns = list(balance_sheet.T.iloc[0])
    st.write(bs)
    st.subheader('Income Statement')
    income_statement = fd.get_income_statement_annual(ticker)[0]
    isl = income_statement.T[2:]
    isl.columns = list(income_statement.T.iloc[0])
    st.write(isl)
    st.subheader('Cash Flow Statement')
    cash_flow = fd.get_cash_flow_annual(ticker)[0]
    cf = cash_flow.T[2:]
    cf.columns = list(cash_flow.T.iloc[0])
    st.write(cf)


def obtenir_articles_presse(ticker):
    url = f'https://newsapi.org/v2/everything?q={ticker}&apiKey={api_key}'
    response = requests.get(url)

    if response.status_code == 200:
        data = response.json()
        if 'articles' in data:
            articles = [article['url'] for article in data['articles'][:20]]
            return articles
        else:
            return None
    else:
        st.error(f"Erreur lors de la récupération des articles de presse. Code d'erreur : {response.status_code}")
        return None

def obtenir_contenu_article(url):
    response = requests.get(url)
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'lxml')
        article_content = ' '.join([p.text for p in soup.find_all('p')])
        return article_content
    else:
        return None

def analyse_sentiments(text):
    doc = nlp(text)
    blob = TextBlob(text)
    sentiment = blob.sentiment
    return sentiment


with news:
    articles = obtenir_articles_presse(ticker)

    if articles:
        st.success(f"Press articles related to {ticker} successfully retrieved.")
        st.write("Sentiment analysis :")

        for idx, article_url in enumerate(articles, start=1):
            article_content = obtenir_contenu_article(article_url)
            if article_content:
                sentiments = analyse_sentiments(article_content)
                st.subheader(f"Article {idx} URL :")
                st.write(article_url)
                st.write("Polarity  :", sentiments.polarity)
                st.write("Subjectivity  :", sentiments.subjectivity)
                st.write("\n")
            else:
                st.warning(f"Unable to retrieve the content of the article from the URL : {article_url}")
    else:
        st.warning("Unable to obtain news articles.")




# session_token = 'eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..i7OKxcJbdruuxP1f.oUxdEZI09EHGG7LVuWOf1Qbj8uaqxOV1lG-wt81j7DjWVtwxHbiZMEomRJw9B9cnctqz4LjxG-HAuB6Bd0J4g99T8YfsGyvfY_zPFZ_Oa88bviyHDD777J15FJPc7T6Wp-ftKNH2wWx1U8vX7Pi-gi89VCHT01cWBOS-kxNWJ-_UtFqLRoHKTt_r3cXKTfxdhwGlHcsbAT79kGgwLczesuloSd8ge6yAczX0Z9r5VHKj1hE8W7kEvz7HctPWUJRDW7gkNcrNBy6SyI355bmZg-eDfdV5oAF0ftdu-pTEZtcamTwmkmo6r-UierC49d7yLveKWeSD4SxYbSEXlZhEb49D2pRo1Yj1xb9NNYEQEoeTZCpPsYMlr5IFuL0RaSv6g2MFF-zLm6hrjI1i_AaLanpmkHDXnCtPU5KRdIR0MFiNp3XGurfBgqj3IIIrgWlqJT2i4nyWR9wnWrn3KLQ036doFAYXKNQfKHyozTtO5QbGLIM0GL_Li8hKTmU6jK2aUCPb1M09DSMv5PqsclfV7lai3YklvfKdxQ93nO3OA4Wahheoat0Xx263d3SAvUj_8QRA_ElYKUeH_cnFW9as2GLXBd-1Y6lb0Y0cT5w5yiIICDmZU02U4ZQAKfE6PMQC1bJSPYrCAq98SG80MNU_8B2qzCYZ4-mHbd6IViFv-pD0sajaFnGGmZ41s6ce9uiEwuK13Vv7QuAulsFTl4MF17-y0j9PnxeLvCs2RlAv5Q0Ei06PLfbbuLrZapn6Dx9lpO1zYyKubC2FWJqcbfEpqT4n4exIQQecDgrMoSlpSnpllD7YCsQAzdr-YA38_WSaA9nLeUt0CanSA5OrDoBpVNhC2EdiVR0JhUdjjv-vt396yRI2IaJIXvZZlXwtsvasUXRVqCHWRZ48mKv1uvqKHV2NAtgOevqKpTrVn1y6E-4_IVhRrPabA3ogdWQtoSsebshNR2vbpZK9KzCQtBu3tTIfnzE2LxVbQdQVASTAdsf9mmSBikvZHDDFQrJGdqYOIpROGrKLVrrVoqI9w-N2RJCPtcDH_-kSBDDdcXNmnrw1n0FdGbsItxQhq9DYauURxx34Dq4iA5Cn3I-6tINOLeoLKvWCiV1chSoPZwZW0w-Lp9au7ajZM90jOtGc56Ub1OcdIN1u4yjYnIm-BwHMJX87E25DewZv-IAfWvJZHThSBnXl_hLe029I0DbJEce_uMjo15I34Swp9t2bNUJixWhkKscsOwN_X_t-iVvCb4rkgGOck96KNf4DlNHq5oHuqjFd5ZNQsb2pmzfqpG-cS35xiFlnDYyGevWL3tdvQZbJc5X1lXoOBISUDQZJmNLekC7vJA8onbMtNwvzUJV2_f8f9ipg2bMC3qyUj169p7_OUKaFbSaOo003F4mGH391xOigo7T_hyCBWZpW75ZZXn1G63Kwg59BV_jdpe6_X0IydFRuvBkdQIPL7KQtqnZ_US2kS5o53m72HyOhiEskOYqT4DFcWWSDGzVQTvYXjkivXuXm3Cq-bqBMEoTrnZ3R5jKvjVcRMBu3xVIhMZpWSqmB9OmsWYs7eZzsEbLQjnUz3Kky5kE2VHO4uGe0NYc8aFEvrQrYF1cul8KT7FFgZPcOJn6ht-Ju5Spw2Fs3ZpqmqhVAYXaaos6Pv0ENLNQqcaB1wnr-PYTNo9LgseadDBDHQSJKF0thwz0BrFenHHgKDm7yQlW5Vs-a2xFbS2Vad0sZuVm7KR_XiAANzzGVDmsq7XyOmA6dsM2yHvNOOx_V_Fok9_uBx8Wk1oPxeVDus7UAwbY1RfQdjXnm6U3J5h1urr3JxhmnzQJqBBmBmPS70M5yccWrPZGgZJjkNcj6ot7Ot7as55QO4gMMsjko8I-cdai_JGXFdrrPnbt05RqUBCnoANPCQv1m7EjIpDu8fjBmDWj1fr4AWCS_sGK23ePDUGN3b-3pjz98GqdJnkvbqrp86HXDzJvk34wnAqi_BHkljczVzo8auSzJiboUDwA69oOFt7wWaXio9MHJbV4VQDZ3LEFR4miKgowCjN4QuMdERUpGIahyTLTTo9v5EZ4lykQfZT5q4mJlLynAz47Xi-m1ClikDeQGijAjzOdWZqaxKL3E3dFqWVlu1X_4caNwxGPp2S6NaTQVgaBE3P0swY1ob0LRjydiQlq2FnfZ3OIHJcKHz19m9jkQh9UB9u9DYJV31D1bU3qo3YXIqdtPYT6WW6dRm2SU5_sQtIemrdjkc1iMHSboVvCCj64Av3Takk-pAT3DcKviSIWLuqSdDYuUmNUZINuXj5avA8m8K7eJzwhvdOS-E_Sl5Hbmh4V0BZFnlo0CcPQQGwQinYe8qRWM_wqbAUZTpAxi_wA8imSdpHy9cnt86HI_k-7nXGGhM-Q_Knm3dB6eyano-mExCQ5M8MEHnG0vi9R6tR2YcSzzp5907B2SxT5MILQCRmlrTBKrK_4SZEh7BSf0lwrS-d1_RNZRsBO3WBhR1qgNLJyiLlHXe3YsznqLlj2Ebu9SUi-jSgE77FOJzEvP4U_SuuDeUg-AiOWEa_r91eQugLRoVA6d2UQmT0LJ8CBYfotrtoHRxAFaXNo4Z0C3TIgxRBS7xTymoruLTIfG8yOULiuImoIKc156JcY7nt2CNInJA2DmmNkVa4YZ0jAUNEN30AgteZodlhn_AtXtJOBr_BROGhz-Qc0ZbLlmxtbidChf_SZ6ymwbZ39jaAWmB5Wc5zqQtKQk4HA_FJJSfHAdiM2TkBm1pp4Fcn3KGmn3SgujhS6tc69RVb2TaAXinv8t84UK4oOY6S1X8Fc.dhljGE92Gf8qTGYZWEBonQ'
# api2 = ChatGPT(session_token)
# buy = api2.send_message(f'3 Reasons to buy {ticker} stock')
# sell = api2.send_message(f'3 Reasons to sell {ticker} stock')
# swot = api2.send_message(f'SWOT analysis of {ticker} stock')

# with openai1:
#     buy_reason, sell_reason, swot_analysis = st.tabs(['3 Reasons to buy', '3 Reasons to sell', 'SWOT analysis'])

#     with buy_reason:
#         st.subheader(f'3 reasons on why to BUY {ticker} Stock')
#         st.write(buy['message'])
#     with sell_reason:
#         st.subheader(f'3 reasons on why to SELL {ticker} Stock')
#         st.write(sell['message'])
#     with swot_analysis:
#         st.subheader(f'SWOT Analysis of {ticker} Stock')
#         st.write(swot['message'])



with tech_indicator:
    st.subheader('Technical Analysis Dashboard')
    df = pd.DataFrame()
    ind_list = df.ta.indicators(as_list=True)
    tech_indicator = st.selectbox('Tech Indicator', options = ind_list)
    method = tech_indicator
    indicator = pd.DataFrame(getattr(ta,method)(low=data['Low'], close=data['Close'], high=data['High'], open=data['Open'], volume=data['Volume']))
    figW_ind_new = px.line(indicator)
    st.plotly_chart(figW_ind_new)
    st.write(indicator)



# Trading Strategy Parameters
amount = 1000
core_trade_amount = amount * 0.80
trade_amount = amount * 0.20
core_to_trade = True
core_quantity = 0
portfolio = 0
investment, real_time_portfolio_value, closes, highs, lows, opens = [], [], [], [], [], []
money_end = amount

# Functions for buy and sell
def buy(allocated_money, price):
    global money_end, portfolio
    quantity = allocated_money / price
    money_end -= quantity * price
    portfolio += quantity
    if investment == []:
        investment.append(allocated_money)
    else:
        investment.append(allocated_money)
        investment[-1] += investment[-2]

def sell(allocated_money, price):
    global money_end, portfolio
    quantity = allocated_money / price
    money_end += allocated_money
    portfolio -= quantity
    investment.append(-allocated_money)
    investment[-1] += investment[-2]

# Function for on_close
def on_close(ws, close_status_code, close_msg):
    portfolio_value = portfolio * closes[-1]
    if portfolio_value > 0:
        sell(portfolio_value, price=closes[-1])
    else:
        buy(-portfolio_value, price=closes[-1])
    money_end += investment[-1]
    st.write("All trades settled")

# Function for on_message
minute_counter = 1
def on_message(ws, message):
    
    global closes, highs, lows, opens, core_to_trade, core_quantity, money_end, portfolio, investment, real_time_portfolio_value, minute_counter 
    json_message = json.loads(message)
    cs = json_message['k']
    candle_closed, close, open, low, high = cs['x'], cs['c'], cs['o'], cs['l'], cs['h']

    if candle_closed:
        closes.append(float(close))
        highs.append(float(high))
        lows.append(float(low))
        opens.append(float(open))
        last_price = closes[-1]

        if core_to_trade:
            buy(core_trade_amount, price=last_price)
            st.write(f'Core Investment: We bought ${core_trade_amount} worth of bitcoin')
            core_quantity += core_trade_amount / last_price
            core_to_trade = False

        engulfing = talib.CDLENGULFING(np.array(opens), np.array(highs), np.array(lows), np.array(closes))
        last_eng = engulfing[-1]
        amt = last_eng * trade_amount / 100
        port_value = (portfolio - core_quantity) * last_price
        trade_amt = amt - port_value

        RT_portfolio_value = money_end + portfolio * last_price
        real_time_portfolio_value.append(float(RT_portfolio_value))


        st.subheader(f'Minute {minute_counter}')
        st.write(f'The Last Engulfing Value is "{last_eng}" and recommended exposure is "${trade_amt}"')
        st.write(f'The Real-Time Portfolio Value: ${RT_portfolio_value}')

        if trade_amt >= 0:
            buy(trade_amt, price=last_price)
            st.write(f'We bought ${trade_amt} worth of bitcoin')
        elif trade_amt < 0:
            sell(-trade_amt, price=last_price)
            st.write(f'We sold ${-trade_amt} worth of bitcoin')

        minute_counter += 1



with bott:
    st.subheader('Trading Bot Dashboard')
    if st.button("Start Trading Bot"):
        ws = websocket.WebSocketApp('wss://stream.binance.com:9443/ws/btcusdt@kline_1m', on_message=on_message, on_close=on_close)
        ws.run_forever()
    

