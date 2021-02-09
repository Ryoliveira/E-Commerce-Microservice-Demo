FROM node:alpine

WORKDIR /app

COPY package.json .
COPY package-lock.json .

RUN npm install

COPY . .

RUN npm run build --prod 

EXPOSE 4200

CMD /app/node_modules/.bin/ng serve --host 0.0.0.0 --disableHostCheck