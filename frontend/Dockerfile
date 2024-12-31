# Use a imagem do Node.js (alpine para um tamanho menor)
FROM node:18-alpine

# Define o diretório de trabalho no container
WORKDIR /app
COPY package*.json ./

# Instala as dependências do projeto
RUN npm install
COPY . .
EXPOSE 4173

CMD ["npm", "run", "dev"]
