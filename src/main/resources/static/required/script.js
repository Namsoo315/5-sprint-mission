// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/user/findAll`,
    BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const users = await response.json();
        renderUserGrid(users);
    } catch (error) {
        console.error('Error fetching users:', error);
        const userGridElement = document.getElementById('userGrid');
        userGridElement.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; color: rgba(255,255,255,0.7); padding: 40px;">
                사용자를 불러올 수 없습니다.
            </div>
        `;
    }
}

// Fetch user profile image
async function fetchUserProfile(profileId) {
    try {
        const response = await fetch(`${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`);
        if (!response.ok) throw new Error('Failed to fetch profile');
        const profile = await response.json();

        return `data:${profile.contentType};base64,${profile.binaryContent}`;
    } catch (error) {
        console.error('Error fetching profile:', error);
        return generateDefaultAvatar();
    }
}

// Generate a default avatar based on username
function generateDefaultAvatar(username = 'U') {
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FECA57', '#FF9FF3', '#54A0FF'];
    const color = colors[username.charCodeAt(0) % colors.length];
    const initial = username.charAt(0).toUpperCase();

    return `data:image/svg+xml;base64,${btoa(`
        <svg width="64" height="64" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="32" cy="32" r="32" fill="${color}"/>
            <text x="50%" y="50%" fill="white" font-size="24" text-anchor="middle" dy=".3em" font-family="sans-serif" font-weight="bold">${initial}</text>
        </svg>
    `)}`;
}

// Render user grid
async function renderUserGrid(users) {
    const userGridElement = document.getElementById('userGrid');
    userGridElement.innerHTML = '';

    if (!users || users.length === 0) {
        userGridElement.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; color: rgba(255,255,255,0.7); padding: 40px;">
                등록된 사용자가 없습니다.
            </div>
        `;
        return;
    }

    for (const user of users) {
        const userCard = document.createElement('div');
        userCard.className = 'user-card';

        const profileUrl = user.profileId ?
            await fetchUserProfile(user.profileId) :
            generateDefaultAvatar(user.username);

        const statusClass = user.online ? 'active' : 'offline';

        userCard.innerHTML = `
            <div class="status-indicator ${statusClass}"></div>
            <div class="user-header">
                <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
                <div class="user-info">
                    <div class="user-name">${user.username}</div>
                    <div class="user-email">${user.email}</div>
                </div>
            </div>
            <div class="user-actions">
                <button class="action-btn primary-btn">메시지</button>
                <button class="action-btn secondary-btn">프로필</button>
            </div>
        `;

        const buttons = userCard.querySelectorAll('.action-btn');
        buttons.forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                const action = btn.textContent.trim();
                console.log(`${action} clicked for user: ${user.username}`);
            });
        });

        userGridElement.appendChild(userCard);
    }
}
