# form

- 后端

  ```java
  @RequestMapping(value = "/add", method=RequestMethod.POST)  
  public String save(@ModelAttribute(value="message") Message message) {  }
  ```

- html

  - @{/add}: 会解析成 /${path}/add

  ```html
  <form th:action="@{/add}" th:object="${message}" method="post">
     <input type="text" th:field="*{info}" />
  </form>
  ```

