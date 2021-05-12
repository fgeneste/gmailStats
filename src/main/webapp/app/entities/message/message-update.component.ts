import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AttachementService from '@/entities/attachement/attachement.service';
import { IAttachement } from '@/shared/model/attachement.model';

import { IMessage, Message } from '@/shared/model/message.model';
import MessageService from './message.service';

const validations: any = {
  message: {
    account: {},
    from: {},
    object: {},
    corps: {},
    date: {},
    stillOnServer: {},
  },
};

@Component({
  validations,
})
export default class MessageUpdate extends Vue {
  @Inject('messageService') private messageService: () => MessageService;
  public message: IMessage = new Message();

  @Inject('attachementService') private attachementService: () => AttachementService;

  public attachements: IAttachement[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.messageId) {
        vm.retrieveMessage(to.params.messageId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.message.id) {
      this.messageService()
        .update(this.message)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.message.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.messageService()
        .create(this.message)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.message.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.message[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.message[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.message[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.message[field] = null;
    }
  }

  public retrieveMessage(messageId): void {
    this.messageService()
      .find(messageId)
      .then(res => {
        res.date = new Date(res.date);
        this.message = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.attachementService()
      .retrieve()
      .then(res => {
        this.attachements = res.data;
      });
  }
}
