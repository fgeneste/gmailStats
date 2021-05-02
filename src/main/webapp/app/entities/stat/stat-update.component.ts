import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import { IStat, Stat } from '@/shared/model/stat.model';
import StatService from './stat.service';

const validations: any = {
  stat: {
    from: {},
    number: {},
    lastupdated: {},
  },
};

@Component({
  validations,
})
export default class StatUpdate extends Vue {
  @Inject('statService') private statService: () => StatService;
  public stat: IStat = new Stat();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.statId) {
        vm.retrieveStat(to.params.statId);
      }
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
    if (this.stat.id) {
      this.statService()
        .update(this.stat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.stat.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.statService()
        .create(this.stat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.stat.created', { param: param.id });
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
      this.stat[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.stat[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.stat[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.stat[field] = null;
    }
  }

  public retrieveStat(statId): void {
    this.statService()
      .find(statId)
      .then(res => {
        res.lastupdated = new Date(res.lastupdated);
        this.stat = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
