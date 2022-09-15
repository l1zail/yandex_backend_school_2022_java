## Spring Boot REST API сервис с Maven, который позволяет пользователям загружать и обновлять информацию о файлах и папках.  

Вступительное задание в __Осеннюю Школу Бэкенд Разработки Яндекса 2022__  
Реализованы базовые задачи и дополнительная задача:  
- __GET__ nodes/{id}
- __DELETE__ delete/{id}
- __POST__ imports
- __GET__ updates  
---
### Развертывание:
``` 
  git clone https://github.com/l92169/yandex_backend_school_2022_java.git  
  cd yandex_backend_school_2022_java/yandex_backend_school_2022_java  
  mvn clean package  
  sudo docker build -t docker-yandex-1 .  
  sudo docker run -d -p 8080:8080 docker-yandex-1  
```
