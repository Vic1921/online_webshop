FROM node:18.13-alpine as builder

WORKDIR /usr/src/app

COPY . /usr/src/app/
RUN npm install
RUN npm run build

FROM nginx:stable-alpine

COPY --from=builder /usr/src/app/dist/online-webshop/browser /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
COPY nginx-selfsigned.crt /etc/nginx/ssl/nginx-selfsigned.crt
COPY nginx-selfsigned.key /etc/nginx/ssl/nginx-selfsigned.key

EXPOSE 443

CMD nginx -g "daemon off;"
