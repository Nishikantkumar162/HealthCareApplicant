# Stage 1: Build React app
FROM node:20-alpine AS build
WORKDIR /app

# Install dependencies first
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the app
COPY . .

# Ensure vite binary is executable
RUN chmod +x node_modules/.bin/vite

# Build the app
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
