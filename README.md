To launch the application, first, ensure you are in the root directory (navigate to the "online_webshop"). 
For the backend execute `docker build --no-cache -t wst2 backend/`. For the frontend, remain in the root and 
run `docker build --no-cache -t wstfe frontend/online-shop/`. The `--no-cache` option is optional but ensures a fresh build. 

Once both images are built, you can start the application using `docker compose up`. Allow approximately 30-40 seconds for initialization. 

Access the application via `https://localhost:443` on your browser. Due to the self-signed certificate, you may need to approve a security 
exception to proceed. The deployment is secure, utilizing HTTPS, with careful port exposure, data volume management and networks for both 
client and database. Moreover, each service is independently deployable locally, fulfilling the configuration of infrastructure requirements, 
and the build-pipeline is entirely contained within Docker, adhering to modern deployment practices.