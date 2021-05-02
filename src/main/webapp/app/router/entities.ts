import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Message = () => import('@/entities/message/message.vue');
// prettier-ignore
const MessageUpdate = () => import('@/entities/message/message-update.vue');
// prettier-ignore
const MessageDetails = () => import('@/entities/message/message-details.vue');
// prettier-ignore
const Stat = () => import('@/entities/stat/stat.vue');
// prettier-ignore
const StatUpdate = () => import('@/entities/stat/stat-update.vue');
// prettier-ignore
const StatDetails = () => import('@/entities/stat/stat-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/message',
    name: 'Message',
    component: Message,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/message/new',
    name: 'MessageCreate',
    component: MessageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/message/:messageId/edit',
    name: 'MessageEdit',
    component: MessageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/message/:messageId/view',
    name: 'MessageView',
    component: MessageDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/stat',
    name: 'Stat',
    component: Stat,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/stat/new',
    name: 'StatCreate',
    component: StatUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/stat/:statId/edit',
    name: 'StatEdit',
    component: StatUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/stat/:statId/view',
    name: 'StatView',
    component: StatDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
