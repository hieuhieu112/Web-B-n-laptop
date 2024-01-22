import numpy as np
import pandas as pd
import nltk
from nltk.corpus import stopwords
from nltk.stem.snowball import SnowballStemmer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

df = pd.read_csv('file.csv')

df['NameProduct']=df['NameProduct'].str.lower().replace(['^a-zA-Z0-9']," ",regex=True)

stemr = SnowballStemmer('english') #chuyển từ tiếng anh bỏ bớt các hậu tố

def tokenization(txt):
    tokens = nltk.word_tokenize(txt)
    stemming =[stemr.stem(w) for w in tokens if w not in stopwords.words('english')]
    return " ".join(stemming)

df['NameProduct']=df['NameProduct'].apply (lambda x: tokenization(x))

def cosine_sim(txt1,txt2):
    obj_ifid = TfidfVectorizer(tokenizer=tokenization)
    matrix = obj_ifid.fit_transform([txt1,txt2])
    similarity = cosine_similarity(matrix)[0][1]
    return similarity

def recommendation (query):
    tokenized_query = tokenization(query)
    df['similarity']=df['NameProduct'].apply(lambda x: cosine_sim(tokenized_query,x))
    final_df=df.sort_values(by = ['similarity'], ascending = False)
    # Lấy 4 phần tử đầu tiên
    final_df = final_df.iloc[1:5]
    return final_df
print(recommendation("laptop dell vostro 15 3520"))
