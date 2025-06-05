import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '2s', target: 300 }, // Spike in 2 seconds
    { duration: '5s', target: 300 },
    { duration: '2s', target: 0 },
  ],
};

const BASE_URL = 'http://localhost:8080';
const API_ENDPOINT = '/api/v1/token/create';

export default function () {
  const res = http.post(BASE_URL + API_ENDPOINT, JSON.stringify({
    userId: `user_${__VU}`,
  }), { headers: { 'Content-Type': 'application/json' } });

  check(res, { 'token 발급 성공': (r) => r.status === 200 });
  sleep(1);
}
