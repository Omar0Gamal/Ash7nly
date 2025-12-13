FROM nginx:alpine

LABEL maintainer="Ash7nly"
LABEL description="Ash7nly Frontend - React Application (Placeholder)"
LABEL version="1.0"

# Create a simple placeholder HTML page
RUN mkdir -p /usr/share/nginx/html && \
    echo '<!DOCTYPE html>\n\
<html lang="en">\n\
<head>\n\
    <meta charset="UTF-8">\n\
    <meta name="viewport" content="width=device-width, initial-scale=1.0">\n\
    <title>Ash7nly - Coming Soon</title>\n\
    <style>\n\
        * { margin: 0; padding: 0; box-sizing: border-box; }\n\
        body {\n\
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;\n\
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n\
            min-height: 100vh;\n\
            display: flex;\n\
            align-items: center;\n\
            justify-content: center;\n\
            color: white;\n\
            text-align: center;\n\
        }\n\
        .container {\n\
            padding: 40px;\n\
            max-width: 600px;\n\
        }\n\
        h1 { font-size: 3rem; margin-bottom: 20px; }\n\
        p { font-size: 1.2rem; opacity: 0.9; margin-bottom: 30px; }\n\
        .status {\n\
            background: rgba(255,255,255,0.1);\n\
            padding: 20px;\n\
            border-radius: 10px;\n\
            margin-top: 30px;\n\
        }\n\
        .status h3 { margin-bottom: 10px; }\n\
        .check { color: #90EE90; margin-right: 10px; }\n\
    </style>\n\
</head>\n\
<body>\n\
    <div class="container">\n\
        <h1>🚚 Ash7nly</h1>\n\
        <p>Delivery Management System</p>\n\
        <p>Frontend coming soon! The React application will be added here.</p>\n\
        <div class="status">\n\
            <h3>System Status</h3>\n\
            <p><span class="check">✓</span> Gateway: Running</p>\n\
            <p><span class="check">✓</span> Backend API: <a href="/api/health" style="color: #90EE90;">/api/health</a></p>\n\
            <p><span class="check">✓</span> Frontend: Placeholder Active</p>\n\
        </div>\n\
    </div>\n\
</body>\n\
</html>' > /usr/share/nginx/html/index.html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

CMD ["nginx", "-g", "daemon off;"]

