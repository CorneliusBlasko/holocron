# **Holocron Test Plan**

- Author: Alberto Mut
- Version 1.0. 
- Date: 23/07/2020

## INTRODUCTION

Holocron is a service acting as a wrapper for the SWAPI api (that can be found in https://swapi.dev/). The service retrieves certain information from ships and characters in the Star Wars universe depending on the endpoint used.

## SCOPE

### In scope

The scope of this plan will include checking that the service works as intended and the information retrieved from the endpoint is accurate and its format is correct.

### Out of scope

The quality of the code will not be tested or reviewed as it is not part of the test. The use of tools or frameworks in this application will not be tested either.

## Quality objective

- Ensure that Holocron conforms to requirements.
- Ensure that Holocron meets the quality specifications defined in the requirements.
- Ensure that bugs/issues are identified and fixed before deployment into production.

## Roles and responsibilities

The assignment of the roles and responsibilities of the team testing the application will be left to the discretion of the project manager. However, it's strongly suggested that any modification or fix in the code should be assigned to the author.

## Test methodology

### Overview

We've used Test Driven Development to develop this project, therefore all the tests are written before the actual code and the entire functionality can be tested at the same time.

### Bug triage

We define several type of bugs:

- **Code error**. 
  - In this particular case the bug is caused by a code error.
  - This bug should be resolved via a ticket assigned to the developer.
- **API error**. 
  - This bug is caused by a faulty data source. The service is prepared to return an error message in case anything goes wrong to the data source, so it won't be as disruptive as an unhandled exception.
  - Holocron uses a free API maintained by and single person, so in case of error in the source we should contact them.
- **Infrastructure error**. In this case the bug lies within the application server, virtual machine and such.
  - The fixing of this bug should be discussed with the ops/IT team.

### Suspension Criteria and Resumption Requirements

The tests should only be suspended if a bug prevents the whole testing plan from finishing. This doesn't mean than a single bug should stop the testing; if the bug is trivial or non-breaking the testing will continue.

### Test Completeness

The criteria to achieve test completeness would be:

- 100% code test coverage
- Format validation of the JSON format in the output messages.
- All open bugs are fixed or deemed irrelevant.

The code must be tested locally and remotely.

## Resource & environment needs

### Automation tools

- Postman

### Bug tracking tools

- Jira
- Kanban board
- Trello (optional)

### Test environment

The following software is required to test Holocron:

- Windows 10
- Java 8 or above
- Apache Tomcat
- IntelliJ IDEA
- Postman









