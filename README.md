
 

- IpFilter блокує запити які надходять з певних Ip адрес і виводить інформацію про блокування. 

 

- Якщо Ip є у чорному списку - фільтр повинен вивести `Access disallowed`.

 

-  Якщо доступ дозволений - фільтр пропускає на сторінку де виводиться фраза `Access allowed`. 

 

- Інформація по Ip які повинні блокуватися, зберігаються у файлі `blackList` .

 
 

- Фільтр повинен підхоплювати оновлення у файлі з інформацією по Ip без перезагрузки сервера або програми.