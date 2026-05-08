# Deployment Guide - ArtistsTracker

## Quick Deploy with Docker Compose (Recommended)

The simplest way to deploy the full application stack:

```bash
# Clone the repository
git clone https://github.com/blakevieyra/MyArtistTracker.git
cd MyArtistTracker

# Build and start all services
docker compose up --build -d

# The app is now running at:
#   Frontend: http://localhost (port 80)
#   Backend API: http://localhost:8083
#   MySQL: localhost:3306
```

To stop:
```bash
docker compose down
```

To stop and remove data:
```bash
docker compose down -v
```

---

## Deploy to AWS EC2

### Prerequisites
- An EC2 instance (Ubuntu 22.04+ recommended, t2.medium or larger)
- Security group with ports 80, 8083, and 22 open
- Docker and Docker Compose installed on the instance

### Steps

1. **SSH into your EC2 instance:**
   ```bash
   ssh -i your-key.pem ubuntu@<your-ec2-public-ip>
   ```

2. **Install Docker (if not already installed):**
   ```bash
   sudo apt-get update
   sudo apt-get install -y docker.io docker-compose-plugin
   sudo usermod -aG docker $USER
   # Log out and back in for group change to take effect
   ```

3. **Clone and deploy:**
   ```bash
   git clone https://github.com/blakevieyra/MyArtistTracker.git
   cd MyArtistTracker
   docker compose up --build -d
   ```

4. **Access the app:**
   Navigate to `http://<your-ec2-public-ip>` in your browser.

### Optional: Add a domain name
- Point your domain's A record to the EC2 public IP
- Update `nginx.conf` to set `server_name yourdomain.com`
- Add HTTPS with Let's Encrypt (see SSL section below)

---

## Deploy to DigitalOcean Droplet

1. Create a Droplet (Ubuntu 22.04, 2GB+ RAM)
2. SSH in and follow the same Docker install + clone steps as EC2 above
3. Access at `http://<droplet-ip>`

---

## Deploy to Railway / Render / Fly.io

These platforms support Docker Compose or individual container deployment:

### Railway
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway init
railway up
```

### Render
- Create a new "Blueprint" from your GitHub repo
- Render will auto-detect the `docker-compose.yml`
- Or deploy each service separately as a Web Service

---

## Production Configuration

### Environment Variables

| Variable | Service | Default | Description |
|----------|---------|---------|-------------|
| `MYSQL_ROOT_PASSWORD` | db | `rootpass` | MySQL root password |
| `MYSQL_DATABASE` | db | `artiststrackerdb` | Database name |
| `MYSQL_USER` | db | `artistsuser` | App database user |
| `MYSQL_PASSWORD` | db | `artistsuser` | App database password |
| `SPRING_DATASOURCE_URL` | backend | (set in compose) | JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | backend | `artistsuser` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | backend | `artistsuser` | DB password |

**For production, change all default passwords!**

### Adding SSL/HTTPS

Add a Certbot container or use a reverse proxy like Traefik:

```yaml
# Add to docker-compose.yml for Let's Encrypt SSL
  certbot:
    image: certbot/certbot
    volumes:
      - ./certbot/conf:/etc/letsencrypt
      - ./certbot/www:/var/www/certbot
    command: certonly --webroot -w /var/www/certbot -d yourdomain.com --agree-tos --email you@email.com
```

Update `nginx.conf` for HTTPS:
```nginx
server {
    listen 443 ssl;
    server_name yourdomain.com;
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    # ... rest of config
}

server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$host$request_uri;
}
```

### Default Login

After deployment, use:
- **Username:** `blake`
- **Password:** `blake`

Or register a new account via the Register page.

---

## Architecture (Deployed)

```
                    ┌─────────────────────────┐
                    │     Internet / Users     │
                    └───────────┬─────────────┘
                                │ :80 / :443
                    ┌───────────▼─────────────┐
                    │   Nginx (Frontend)       │
                    │   - Serves Angular SPA   │
                    │   - Proxies /api/ calls  │
                    └───────────┬─────────────┘
                                │ :8083
                    ┌───────────▼─────────────┐
                    │   Spring Boot (Backend)  │
                    │   - REST API             │
                    │   - Authentication       │
                    │   - Business Logic       │
                    └───────────┬─────────────┘
                                │ :3306
                    ┌───────────▼─────────────┐
                    │   MySQL 8.0 (Database)   │
                    │   - artiststrackerdb     │
                    │   - Persistent volume    │
                    └─────────────────────────┘
```

## Troubleshooting

- **Backend can't connect to DB:** Wait 30s after `docker compose up` — MySQL needs time to initialize. The healthcheck handles this automatically.
- **Frontend shows blank page:** Check browser console; ensure the backend is reachable at the configured URL.
- **Port conflicts:** Change port mappings in `docker-compose.yml` (e.g., `"8080:80"` for frontend).
- **Database not seeded:** Remove the volume (`docker compose down -v`) and restart to re-run init scripts.
