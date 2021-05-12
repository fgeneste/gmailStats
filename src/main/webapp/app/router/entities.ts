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
// prettier-ignore
const Attachement = () => import('@/entities/attachement/attachement.vue');
// prettier-ignore
const AttachementUpdate = () => import('@/entities/attachement/attachement-update.vue');
// prettier-ignore
const AttachementDetails = () => import('@/entities/attachement/attachement-details.vue');
// prettier-ignore
const Conf = () => import('@/entities/conf/conf.vue');
// prettier-ignore
const ConfUpdate = () => import('@/entities/conf/conf-update.vue');
// prettier-ignore
const ConfDetails = () => import('@/entities/conf/conf-details.vue');
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
  {
    path: '/attachement',
    name: 'Attachement',
    component: Attachement,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachement/new',
    name: 'AttachementCreate',
    component: AttachementUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachement/:attachementId/edit',
    name: 'AttachementEdit',
    component: AttachementUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachement/:attachementId/view',
    name: 'AttachementView',
    component: AttachementDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/conf',
    name: 'Conf',
    component: Conf,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/conf/new',
    name: 'ConfCreate',
    component: ConfUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/conf/:confId/edit',
    name: 'ConfEdit',
    component: ConfUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/conf/:confId/view',
    name: 'ConfView',
    component: ConfDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
