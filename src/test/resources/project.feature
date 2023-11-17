Feature: Create a user and CRUD for Projects

  Scenario: As a user wants to verify that is possible create a new user and then with this user
  make the crud operations for projects using token

    Given create a new user with body
    """
    {
      "Email": "{randomUser}",
      "FullName": "{randomUser}",
      "Password": "{randomUser}"
    }
    """
    Then the status code should be 200
    And obtain the user token in todo.ly
    When send a POST request to "/api/projects.json" with body
    """
    {
      "Content":"ProjectTest",
      "Icon":2
    }
    """
    Then the status code should be 200
    And the attribute "Content" should have the value "ProjectTest"
    And save the value "Id" in the variable "$ID_PROJECT"
    When send a PUT request to "/api/projects/$ID_PROJECT.json" with body
    """
    {
      "Content":"ProjectTestJPUPDATE"
    }
    """
    Then the status code should be 200
    And the attribute "Content" should have the value "ProjectTestJPUPDATE"
    When send a DELETE request to "/api/projects/$ID_PROJECT.json" with body
    """
    """
    Then the status code should be 200
    And the attribute "Content" should have the value "ProjectTestJPUPDATE"