# # stage 1
# FROM node:latest as node
# WORKDIR /app
# COPY . .
# RUN npm install
# RUN npm run build --prod

# # stage 2
# FROM nginx:alpine
# COPY --from=node /app/dist/ecommerce-shop-ui /usr/share/nginx/html

FROM node:alpine

WORKDIR /app

COPY package.json .
COPY package-lock.json .

RUN npm install

COPY . .

RUN npm run build --prod 

EXPOSE 4200

CMD /app/node_modules/.bin/ng serve --host 0.0.0.0 --disableHostCheck