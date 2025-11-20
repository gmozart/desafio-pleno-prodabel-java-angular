# CI/CD Documentation

This document provides information about the CI/CD workflow, including required secrets, local testing instructions, and branch management guidelines.

## Table of Contents

- [GitHub Actions Workflow Overview](#github-actions-workflow-overview)
- [Required Secrets](#required-secrets)
- [How to Add Secrets in GitHub](#how-to-add-secrets-in-github)
- [Local Testing](#local-testing)
- [Branch Cleanup](#branch-cleanup)

## GitHub Actions Workflow Overview

The CI/CD workflow (`.github/workflows/ci-docker-sonar.yml`) automates the following processes:

1. **Build and Test Job**: 
   - Checks out the code
   - Sets up Java 17 (Temurin distribution)
   - Caches Maven dependencies
   - Runs `mvn -B clean package` to build the project and execute tests
   - Conditionally runs SonarQube analysis if secrets are configured

2. **Docker Job**:
   - Runs only on push events to `develop` or `main` branches
   - Builds the Docker image using the project's Dockerfile
   - Pushes the image to GitHub Container Registry (GHCR)
   - Tags images with:
     - Git commit SHA (full)
     - `latest` tag for develop/main branches

### Workflow Triggers

- Push to `develop` or `main` branches
- Pull requests targeting `develop` or `main` branches

### Permissions

The workflow requires:
- `contents: read` - To read repository contents
- `packages: write` - To push Docker images to GHCR

## Required Secrets

The workflow uses the following secrets:

### SonarQube Configuration (Optional)

| Secret Name | Description | Required |
|-------------|-------------|----------|
| `SONAR_TOKEN` | Authentication token for SonarQube/SonarCloud | Optional |
| `SONAR_HOST_URL` | URL of your SonarQube server (e.g., `https://sonarcloud.io`) | Optional |

**Note**: The SonarQube analysis step will only run if both `SONAR_TOKEN` and `SONAR_HOST_URL` are configured. The workflow will continue even if this step fails.

### Docker Registry Configuration

| Secret Name | Description | Required |
|-------------|-------------|----------|
| `GITHUB_TOKEN` | Automatically provided by GitHub Actions | Yes (automatic) |

**Note**: For GitHub Container Registry (GHCR), the workflow uses the automatically provided `GITHUB_TOKEN`. No additional configuration is needed.

### Alternative Registry Configuration (Optional)

If you prefer to use a different container registry (e.g., Docker Hub, Azure Container Registry):

| Secret Name | Description |
|-------------|-------------|
| `REGISTRY_URL` | URL of the container registry |
| `REGISTRY_USERNAME` | Username for registry authentication |
| `REGISTRY_PASSWORD` | Password or token for registry authentication |

To use an alternative registry, modify the login step in the workflow file:

```yaml
- name: Log in to Container Registry
  uses: docker/login-action@v3
  with:
    registry: ${{ secrets.REGISTRY_URL }}
    username: ${{ secrets.REGISTRY_USERNAME }}
    password: ${{ secrets.REGISTRY_PASSWORD }}
```

## How to Add Secrets in GitHub

1. Navigate to your repository on GitHub
2. Click on **Settings** (in the repository menu)
3. In the left sidebar, click on **Secrets and variables** > **Actions**
4. Click on **New repository secret**
5. Enter the secret name (e.g., `SONAR_TOKEN`)
6. Enter the secret value
7. Click **Add secret**

Repeat these steps for each secret you need to configure.

### SonarQube Setup Instructions

If you want to enable SonarQube analysis:

1. Create a SonarQube/SonarCloud account
2. Create a new project and obtain the project key
3. Generate an authentication token
4. Add the following secrets to your GitHub repository:
   - `SONAR_TOKEN`: Your SonarQube authentication token
   - `SONAR_HOST_URL`: Your SonarQube server URL (e.g., `https://sonarcloud.io`)
5. Update the workflow file with your project-specific configuration if needed

## Local Testing

Before pushing changes, you can test the application locally using Maven, Docker, and docker-compose.

### Prerequisites

- Java 17 (JDK)
- Maven 3.6+
- Docker
- docker-compose

### Maven Build and Test

To build and test the application locally:

```bash
# Clean and build the project
./mvnw clean package

# Run tests only
./mvnw test

# Run tests with coverage
./mvnw verify

# Skip tests (for faster builds)
./mvnw clean package -DskipTests
```

### Docker Build

To build the Docker image locally:

```bash
# Build the image
docker build -t desafio-backend:local .

# Run the container
docker run -p 8080:8080 desafio-backend:local
```

### Docker Compose

To run the application with all dependencies (PostgreSQL database):

```bash
# Start all services
docker-compose -f docker/docker-compose.yml up -d

# Build and start the application
./mvnw spring-boot:run

# Or build the Docker image and run everything together
docker build -t desafio-backend:local .
docker run --network host desafio-backend:local

# Stop all services
docker-compose -f docker/docker-compose.yml down

# Stop and remove volumes (database data)
docker-compose -f docker/docker-compose.yml down -v
```

### Testing the API

Once the application is running, you can access:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

### Verifying Docker Image

To verify the Docker image works correctly:

```bash
# Build the image
docker build -t desafio-backend:test .

# Run the container
docker run -p 8080:8080 desafio-backend:test

# Check logs
docker logs <container-id>

# Test the health endpoint
curl http://localhost:8080/actuator/health
```

## Branch Cleanup

After your pull request is merged, it's important to clean up temporary branches to keep the repository organized.

### Delete Local Branch

After your PR is merged into `develop` or `main`:

```bash
# Switch to develop branch
git checkout develop

# Pull the latest changes
git pull origin develop

# Delete the local branch
git branch -d <your-branch-name>

# If the branch wasn't fully merged, force delete (use with caution)
git branch -D <your-branch-name>
```

### Delete Remote Branch

GitHub typically offers an option to delete the branch automatically after merging the PR. If not deleted automatically:

```bash
# Delete the remote branch
git push origin --delete <your-branch-name>
```

Or use the GitHub web interface:
1. Go to your repository on GitHub
2. Click on **Branches** (in the Code tab)
3. Find your merged branch
4. Click the trash icon to delete it

### Best Practices

- **Delete branches after merge**: Keep your repository clean by removing branches that have been merged
- **Use descriptive branch names**: Follow naming conventions like `feature/`, `bugfix/`, `chore/`, etc.
- **Regular cleanup**: Periodically review and delete old branches that are no longer needed
- **Protected branches**: Never delete `main`, `develop`, or other protected branches

### Automatic Branch Cleanup

You can enable automatic branch deletion in GitHub:

1. Go to **Settings** > **General**
2. Scroll to **Pull Requests**
3. Check **Automatically delete head branches**

This will automatically delete feature branches after PR merges.

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)

## Support

For issues or questions regarding the CI/CD pipeline:

1. Check the workflow run logs in the **Actions** tab
2. Review this documentation
3. Create an issue in the repository
4. Contact the DevOps team

---
