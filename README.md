## REST API сервис, который позволяет пользователям загружать и обновлять информацию о файлах и папках.  

Вступительное задание в __Осеннюю Школу Бэкенд Разработки Яндекса 2022__  
Реализованы базовые задачи и дополнительная задача:  
- __GET__ nodes/{id}
- __DELETE__ delete/{id}
- __POST__ imports
- __GET__ updates  
---
### Развертывание:
1. git clone https://github.com/l92169/yandex_backend_school_2022_java.git
2. cd yandex_backend_school_2022_java/yandex_backend_school_2022_java
3. mvn clean package
4. sudo docker build -t docker-yandex-1 . 
5. sudo docker run -d -p 8080:8080 docker-yandex-1
